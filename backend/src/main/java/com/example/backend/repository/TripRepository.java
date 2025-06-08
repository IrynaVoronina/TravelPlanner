package com.example.backend.repository;

import com.example.backend.model.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
}
