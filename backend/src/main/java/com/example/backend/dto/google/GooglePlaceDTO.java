package com.example.backend.dto.google;

import com.example.backend.model.enums.PlaceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GooglePlaceDTO {

    PlaceType placeType;

    String name;
    String description;
    Double rating;

    LocalTime openingTime;
    LocalTime closingTime;
    List<GoogleReviewDTO> reviews;
    Double latitude;
    Double longitude;
}