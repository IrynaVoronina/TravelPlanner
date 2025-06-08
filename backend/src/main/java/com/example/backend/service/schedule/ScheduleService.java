package com.example.backend.service.schedule;

import com.example.backend.model.entities.Schedule;
import com.example.backend.model.enums.TravelMode;

import java.time.LocalTime;
import java.util.List;


public interface ScheduleService {

    Schedule getById(Integer id);

    void resetStartAndEndTimesByTripId(int tripId);

    List<Schedule> createFullScheduleByTrip(int tripId, LocalTime startTime, TravelMode travelMode);

    List<Schedule> findByTripIdAndActivityId(int tripId, int placeId);

    List<Schedule> getAllSchedulesByTrip(int tripId);

}
