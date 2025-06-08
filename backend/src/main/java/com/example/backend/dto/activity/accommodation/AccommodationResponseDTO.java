package com.example.backend.dto.activity.accommodation;

import com.example.backend.model.enums.AccommodationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccommodationResponseDTO {

    Integer id;
    String name;
    String description;
    AccommodationType accommodationType;

    double pricePerNight;
    int stars;

    boolean wifiAvailable;
    boolean parkingAvailable;
    boolean petFriendly;

    Double latitude;
    Double longitude;
}
