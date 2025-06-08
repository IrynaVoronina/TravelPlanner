package com.example.backend.dto.google;

import com.example.backend.model.entities.Location;
import com.example.backend.model.enums.TravelMode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleRouteSegmentDTO {
    Location startPoint;
    Location endPoint;
    int staticDuration;
    int distanceMeters;
    TravelMode travelMode;
}