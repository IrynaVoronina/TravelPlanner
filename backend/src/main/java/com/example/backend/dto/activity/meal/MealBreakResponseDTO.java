package com.example.backend.dto.activity.meal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealBreakResponseDTO {
    Integer id;
    String name;
    String description;

    String startTime;
    String endTime;
    int duration;

    Double latitude;
    Double longitude;
}
