package com.example.backend.dto.activity.place;

import com.example.backend.dto.review.ReviewResponseDTO;
import com.example.backend.model.enums.PlaceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaceResponseDTO {

    Integer id;

    PlaceType placeType;

    String name;
    String description;
    double rating;

    String openingTime;
    String closingTime;
    int visitDuration;

    List<ReviewResponseDTO> reviews;

    Double latitude;
    Double longitude;
}



