package com.example.backend.dto.activity.meal;

import com.example.backend.model.enums.MealBreakType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealBreakRequestDTO {

    MealBreakType name;
    String description;

    @NotNull(message = "Start Time must be specified")
    @Pattern(regexp = "^([0-1]\\d|2[0-3]):([0-5]\\d)$",
            message = "Time must be in the format HH:mm (24-hour format)")
    String startTime;

    @NotNull(message = "End Time must be specified")
    @Pattern(regexp = "^([0-1]\\d|2[0-3]):([0-5]\\d)$",
            message = "Time must be in the format HH:mm (24-hour format)")
    String endTime;
}
