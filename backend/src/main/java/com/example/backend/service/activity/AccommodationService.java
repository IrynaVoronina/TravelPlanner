package com.example.backend.service.activity;

import com.example.backend.model.entities.Accommodation;
import com.example.backend.model.entities.Trip;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.service.trip.TripService;
import com.example.backend.validation.AccommodationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccommodationService extends ActivityService<Accommodation> {


    public AccommodationService(ActivityRepository activityRepository,
                                TripService tripService,
                                ScheduleRepository scheduleRepository) {
        super(activityRepository, tripService, scheduleRepository);
    }

    @Override
    public Accommodation addActivity(Accommodation accommodation, Integer tripId) {
        List<Accommodation> accommodationByTrip = super.getActivities(tripId);
        if (!accommodationByTrip.isEmpty()) {
            throw new AccommodationException("Accommodation already exists for this trip");
        }

        Accommodation nawAccommodation = activityRepository.save(accommodation);
        Trip trip = tripService.getById(tripId);
        super.addActivityToSchedule(nawAccommodation, trip);
        super.addActivityToSchedule(nawAccommodation, trip);
        return nawAccommodation;
    }

    @Override
    protected Class<Accommodation> getActivityType() {
        return Accommodation.class;
    }
}
