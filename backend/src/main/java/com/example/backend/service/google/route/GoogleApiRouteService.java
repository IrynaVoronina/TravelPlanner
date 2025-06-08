package com.example.backend.service.google.route;


import com.example.backend.dto.google.GoogleRouteDTO;
import com.example.backend.model.entities.Location;
import com.example.backend.model.enums.TravelMode;

public interface GoogleApiRouteService {
    GoogleRouteDTO getRoute(Location origin, Location destination, TravelMode travelMode);
}
