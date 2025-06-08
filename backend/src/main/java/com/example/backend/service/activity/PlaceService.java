package com.example.backend.service.activity;

import com.example.backend.model.entities.Place;
import com.example.backend.model.entities.Trip;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.service.trip.TripService;
import org.springframework.stereotype.Service;


@Service
public class PlaceService extends ActivityService<Place> {


    public PlaceService(ActivityRepository activityRepository,
                        TripService tripService,
                        ScheduleRepository scheduleRepository) {
        super(activityRepository, tripService, scheduleRepository);
    }

    @Override
    public Place addActivity(Place activity, Integer tripId) {
        Place newPlace = activityRepository.save(activity);
        Trip trip = tripService.getById(tripId);
        super.addActivityToSchedule(newPlace, trip);
        return newPlace;
    }

    @Override
    protected Class<Place> getActivityType() {
        return Place.class;
    }
}
