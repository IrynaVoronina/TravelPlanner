package com.example.backend.service.activity;

import com.example.backend.model.entities.Activity;
import com.example.backend.model.entities.Schedule;
import com.example.backend.model.entities.Trip;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.service.trip.TripService;
import com.example.backend.validation.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class ActivityService<T extends Activity> {

    protected ActivityRepository activityRepository;
    protected TripService tripService;
    protected ScheduleRepository scheduleRepository;

    public T getActivityById(int id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
        if (getActivityType().isInstance(activity)) {
            return getActivityType().cast(activity);
        }
        throw new ResourceNotFoundException("Activity with id " + id + " is not a " + getActivityType().getSimpleName());
    }

    public List<T> getActivities(int tripId) {
        return scheduleRepository.findActivitiesByTripId(tripId).stream()
                .filter(getActivityType()::isInstance)
                .map(getActivityType()::cast)
                .collect(Collectors.toList());
    }

    @Transactional
    public abstract T addActivity(T activity, Integer tripId);

    @Transactional
    public void deleteActivity(List<Schedule> schedules, int activityId) {
        getActivityById(activityId);

        activityRepository.deleteById(activityId);
        scheduleRepository.deleteAll(schedules);
    }

    protected Schedule addActivityToSchedule(T activity, Trip trip) {
        Schedule schedule = Schedule.builder().trip(trip).activity(activity).build();
        return scheduleRepository.save(schedule);
    }

    public T updateActivity(T activity) {
        T activityById = getActivityById(activity.getId());
        return activityRepository.save(activity);
    }

    protected abstract Class<T> getActivityType();
}

