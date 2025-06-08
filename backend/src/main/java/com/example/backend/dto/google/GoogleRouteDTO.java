package com.example.backend.dto.google;

import com.example.backend.model.entities.Location;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleRouteDTO {
    int totalDuration;
    Location startPlace;
    Location endPlace;
    List<GoogleRouteSegmentDTO> segments;
}
