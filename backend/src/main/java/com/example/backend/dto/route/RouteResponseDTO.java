package com.example.backend.dto.route;

import com.example.backend.model.entities.Location;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteResponseDTO {
    Integer id;
    int totalDuration;
    Location startPlace;
    Location endPlace;
    List<RouteSegmentResponseDTO> segments;
}
