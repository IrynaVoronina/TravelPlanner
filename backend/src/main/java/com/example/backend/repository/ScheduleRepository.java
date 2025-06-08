package com.example.backend.repository;

import com.example.backend.model.entities.Activity;
import com.example.backend.model.entities.Schedule;
import com.example.backend.model.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s.activity FROM Schedule s WHERE s.trip.id = :tripId")
    List<Activity> findActivitiesByTripId(@Param("tripId") Integer tripId);

    List<Schedule> findByTripIdAndActivityId(int tripId, int activityId);

    List<Schedule> findByTripId(int tripId);

    List<Schedule> findAllByTrip(Trip trip);


}
