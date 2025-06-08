package com.example.backend.dto.schedule;

import com.example.backend.dto.activity.ActivityResponseDTO;
import com.example.backend.dto.trip.TripResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleResponseDTO {

    Integer id;

    String startTime;
    String endTime;
    TripResponseDTO trip;
    ActivityResponseDTO activity;
}