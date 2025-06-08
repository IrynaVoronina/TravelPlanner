package com.example.backend.service.activity;

import com.example.backend.model.entities.MealBreak;
import com.example.backend.model.entities.Schedule;
import com.example.backend.model.entities.Trip;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.service.trip.TripService;
import com.example.backend.validation.MealBreakException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
public class MealBreakService extends ActivityService<MealBreak> {


    public MealBreakService(ActivityRepository activityRepository,
                            TripService tripService,
                            ScheduleRepository scheduleRepository) {
        super(activityRepository, tripService, scheduleRepository);
    }

    @Override
    public MealBreak addActivity(MealBreak mealBreak, Integer tripId) {
        List<MealBreak> mealBreaks = super.getActivities(tripId);
        boolean exists = mealBreaks.stream()
                .anyMatch(existing -> existing.getName().equals(mealBreak.getName()));

        if (exists) {
            throw new MealBreakException("A meal break with type '" + mealBreak.getName() + "' already exists for this trip.");
        }
        mealBreak.setDuration(calculateMealBreakDuration(mealBreak));

        MealBreak newMealBreak = activityRepository.save(mealBreak);
        Trip trip = tripService.getById(tripId);

        Schedule schedule = super.addActivityToSchedule(newMealBreak, trip);
        schedule.setStartTime(mealBreak.getStartTime());
        schedule.setEndTime(mealBreak.getEndTime());
        scheduleRepository.save(schedule);
        return newMealBreak;
    }

    private int calculateMealBreakDuration(MealBreak mealBreak) {
        LocalTime start = mealBreak.getStartTime();
        LocalTime end = mealBreak.getEndTime();
        return (int) java.time.Duration.between(start, end).toMinutes();
    }

    @Override
    protected Class<MealBreak> getActivityType() {
        return MealBreak.class;
    }
}
