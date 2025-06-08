package com.example.backend.dto.route;

import com.example.backend.model.entities.Location;
import com.example.backend.model.enums.TravelMode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteSegmentResponseDTO {
    Location startPoint;
    Location endPoint;
    int staticDuration;
    int distanceMeters;
    TravelMode travelMode;
}
