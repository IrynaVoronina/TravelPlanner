package com.example.backend.service.route;

import com.example.backend.model.entities.Accommodation;
import com.example.backend.model.entities.MealBreak;
import com.example.backend.model.entities.Place;
import com.example.backend.model.entities.Route;
import com.example.backend.model.enums.TravelMode;

import java.time.LocalTime;
import java.util.List;

public interface RouteService {

    List<Route> findOptimalRouteSequence(List<Place> places,
                                         Accommodation accommodation,
                                         List<MealBreak> mealBreaks,
                                         int tripId,
                                         LocalTime startTime,
                                         TravelMode travelMode);

    List<Route> saveRouteSequence(List<Route> routes, int tripId);

    List<Route> getAllByTrip(int tripId);
}
