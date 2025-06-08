package com.example.backend.service.google.weather;

import com.example.backend.dto.google.WeatherRecommendationDTO;
import com.example.backend.model.entities.Location;

import java.time.LocalDate;

public interface WeatherService {
    WeatherRecommendationDTO getWeatherRecommendation(Location location, LocalDate date);
}