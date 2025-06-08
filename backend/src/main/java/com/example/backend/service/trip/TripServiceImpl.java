package com.example.backend.service.trip;

import com.example.backend.model.entities.Trip;
import com.example.backend.repository.TripRepository;
import com.example.backend.validation.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService{

    private final TripRepository tripRepository;

    @Override
    public List<Trip> getAll() {
        return tripRepository.findAll();
    }

    @Override
    public Trip getById(Integer id) {
        return getOrElseThrow(id);
    }

    private Trip getOrElseThrow(Integer id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Trip with id %s does not exist", id)));
    }

    @Override
    public Trip create(Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    public Trip update(Trip trip) {
        getOrElseThrow(trip.getId());
        return tripRepository.save(trip);
    }

    @Override
    public void delete(Integer id) {
        getOrElseThrow(id);
        tripRepository.deleteById(id);
    }

    @Override
    public Trip cloneFullTrip(Integer tripId) {
        Trip original = getOrElseThrow(tripId);
        Trip clonedTrip = original.clone();
        tripRepository.save(clonedTrip);
        return clonedTrip;
    }
}
