package com.example.backend.service.google.place;

import com.example.backend.dto.google.GooglePlaceDTO;
import com.example.backend.model.entities.Location;

import java.util.List;

public interface GoogleApiPlaceService {

    Location getCoordinates(String address);

    List<GooglePlaceDTO> getDetailedPlaces(String city, String categoryType, int radius);
}
