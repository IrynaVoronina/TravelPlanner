package com.example.backend.controller;

import com.example.backend.dto.google.WeatherRecommendationDTO;
import com.example.backend.model.entities.Location;
import com.example.backend.service.google.place.GoogleApiPlaceService;
import com.example.backend.service.google.weather.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final GoogleApiPlaceService googleApiPlaceService;


    @PostMapping("/check")
    public ResponseEntity<WeatherRecommendationDTO> checkWeather(
            @RequestParam String city,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Location coordinatesForCity = googleApiPlaceService.getCoordinates(city);
        System.out.println(coordinatesForCity);
        WeatherRecommendationDTO recommendation = weatherService.getWeatherRecommendation(coordinatesForCity, date);
        return ResponseEntity.ok(recommendation);
    }
}