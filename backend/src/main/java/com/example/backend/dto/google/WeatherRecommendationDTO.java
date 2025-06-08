package com.example.backend.dto.google;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherRecommendationDTO {
    boolean isSuitable;
    String message;
    int precipitationProbability;
    int thunderstormProbability;
    String description;
    String cloudCover;
}
