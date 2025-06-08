package com.example.backend.dto.schedule;

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
public class ScheduleRequestDTO {

    @NotNull(message = "Time must be specified")
    Integer id;

    @NotNull(message = "Time must be specified")
    @Pattern(regexp = "^([0-1]\\d|2[0-3]):([0-5]\\d)$",
            message = "Time must be in the format HH:mm (24-hour format)")
    String startTime;

    @NotNull(message = "Time must be specified")
    @Pattern(regexp = "^([0-1]\\d|2[0-3]):([0-5]\\d)$",
            message = "Time must be in the format HH:mm (24-hour format)")
    String endTime;

}
