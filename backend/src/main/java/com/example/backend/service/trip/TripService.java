package com.example.backend.service.trip;

import com.example.backend.model.entities.Trip;

import java.util.List;

public interface TripService {
    List<Trip> getAll();

    Trip getById(Integer id);

    Trip create(Trip trip);

    Trip update(Trip trip);

    void delete(Integer id);

    Trip cloneFullTrip(Integer tripId);
}
