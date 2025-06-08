package com.example.backend.service.schedule;

import com.example.backend.model.entities.*;
import com.example.backend.model.enums.TravelMode;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.service.activity.ActivityService;
import com.example.backend.service.route.RouteService;
import com.example.backend.service.trip.TripService;
import com.example.backend.validation.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    ScheduleRepository scheduleRepository;
    TripService tripService;
    RouteService routeService;
    @Qualifier("accommodationService")
    ActivityService accommodationService;
    @Qualifier("mealBreakService")
    ActivityService mealBreakService;
    @Qualifier("placeService")
    ActivityService placeService;


    @Override
    public Schedule getById(Integer id) {
        return getOrElseThrow(id);
    }


    private Schedule getOrElseThrow(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("ScheduleLine with id %s does not exist", id)));
    }

    @Override
    @Transactional
    public void resetStartAndEndTimesByTripId(int tripId) {
        List<Schedule> schedules = scheduleRepository.findByTripId(tripId);
        for (Schedule schedule : schedules) {
            schedule.setStartTime(null);
            schedule.setEndTime(null);
        }
        scheduleRepository.saveAll(schedules);
    }

    @Override
    @Transactional
    public List<Schedule> createFullScheduleByTrip(int tripId, LocalTime startTime, TravelMode travelMode) {
        Trip trip = tripService.getById(tripId);

        List<Place> allPlacesByTrip = placeService.getActivities(tripId);
        List<Accommodation> accommodations = accommodationService.getActivities(tripId);
        Accommodation accommodation = accommodations != null && !accommodations.isEmpty() ? accommodations.get(0) : null;

        List<MealBreak> allMealBreaksByTrip = mealBreakService.getActivities(tripId);

        if (allMealBreaksByTrip == null || allMealBreaksByTrip.isEmpty()) {
            allMealBreaksByTrip = Collections.emptyList();
        }

        List<Route> optimalRoutes = routeService.findOptimalRouteSequence(
                allPlacesByTrip, accommodation, allMealBreaksByTrip, tripId, startTime, travelMode);

        List<Schedule> schedules = new ArrayList<>();
        LocalTime currentTime = startTime;


        List<MealBreak> sortedMealBreaks = new ArrayList<>(allMealBreaksByTrip);
        sortedMealBreaks.sort(Comparator.comparing(MealBreak::getStartTime));
        int mealIndex = 0;

        Activity firstActivity = optimalRoutes.get(0).getStart();
        currentTime = roundUpToNearestMinutes(currentTime, 5);
        Schedule builtFirstSchedule = buildSchedule(firstActivity, trip, currentTime);
        schedules.add(builtFirstSchedule);

        currentTime = builtFirstSchedule.getEndTime();

        for (Route route : optimalRoutes) {
            currentTime = currentTime.plusSeconds(route.getTotalDuration());
            currentTime = roundUpToNearestMinutes(currentTime, 5);

            while (mealIndex < sortedMealBreaks.size() &&
                    !currentTime.isBefore(sortedMealBreaks.get(mealIndex).getStartTime()) &&
                    currentTime.isBefore(sortedMealBreaks.get(mealIndex).getEndTime())) {

                MealBreak mealBreak = sortedMealBreaks.get(mealIndex);
                LocalTime mealStart = roundUpToNearestMinutes(mealBreak.getStartTime(), 5);
                schedules.add(buildSchedule(mealBreak, trip, mealStart));
                currentTime = mealBreak.getEndTime();
                mealIndex++;

                currentTime = currentTime.plusSeconds(route.getTotalDuration());
                currentTime = roundUpToNearestMinutes(currentTime, 5);
            }

            Activity activity = route.getEnd();

            Schedule builtSchedule = buildSchedule(activity, trip, currentTime);
            schedules.add(builtSchedule);
            currentTime = builtSchedule.getEndTime();
        }

        return schedules;
    }

    @Override
    public List<Schedule> findByTripIdAndActivityId(int tripId, int activityId) {
        return scheduleRepository.findByTripIdAndActivityId(tripId, activityId);
    }

    private Schedule buildSchedule(Activity activity, Trip trip, LocalTime startTime) {
        int duration = 0;
        List<Schedule> existingSchedules = findByTripIdAndActivityId(trip.getId(), activity.getId());
        Schedule schedule;

        if (activity instanceof Accommodation) {
            if (existingSchedules.get(0).getStartTime() == null && existingSchedules.get(1).getStartTime() == null) {
                schedule = existingSchedules.get(0);
                schedule.setStartTime(LocalTime.of(0, 0));
                schedule.setEndTime(startTime);
                System.out.println("start == ");
            } else if (existingSchedules.get(0).getStartTime() != null){
                schedule = existingSchedules.get(1);
                schedule.setStartTime(startTime);
                schedule.setEndTime(LocalTime.of(23, 59));
                System.out.println("end 1 == "  + schedule.getStartTime() + "/" + schedule.getEndTime() );
            } else {
                schedule = existingSchedules.get(0);
                schedule.setStartTime(startTime);
                schedule.setEndTime(LocalTime.of(23, 59));
                System.out.println("end 2 == "  + schedule.getStartTime() + "/" + schedule.getEndTime() );
            }
        } else {
            schedule = existingSchedules.get(0);
            if (activity instanceof MealBreak mealBreak) {
                duration = (int) Duration.between(mealBreak.getStartTime(), mealBreak.getEndTime()).toMinutes();
            } else if (activity instanceof Place place) {
                duration = place.getVisitDuration();
            }
            schedule.setEndTime(startTime.plusMinutes(duration));
            schedule.setStartTime(startTime);
        }
        return scheduleRepository.save(schedule);
    }

    private LocalTime roundUpToNearestMinutes(LocalTime time, int nearestMinutes) {
        int minute = time.getMinute();
        int remainder = minute % nearestMinutes;
        if (remainder == 0 && time.getSecond() == 0) {
            return time.withSecond(0).withNano(0);
        }

        int minutesToAdd = nearestMinutes - remainder;
        return time.plusMinutes(minutesToAdd).withSecond(0).withNano(0);
    }

    @Override
    public List<Schedule> getAllSchedulesByTrip(int tripId) {
        return scheduleRepository.findAllByTrip(tripService.getById(tripId));
    }
}
