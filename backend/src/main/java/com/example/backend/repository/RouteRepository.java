package com.example.backend.repository;

import com.example.backend.model.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    void deleteByTripId(int tripId);
    List<Route> findAllByTripId(int tripId);
}
