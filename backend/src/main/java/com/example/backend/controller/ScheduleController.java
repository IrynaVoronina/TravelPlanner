package com.example.backend.controller;

import com.example.backend.dto.schedule.ScheduleResponseDTO;
import com.example.backend.mapper.ScheduleMapper;
import com.example.backend.model.entities.Schedule;
import com.example.backend.model.enums.TravelMode;
import com.example.backend.service.schedule.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok().body(scheduleMapper.toDto(scheduleService.getById(id)));
    }

    @GetMapping()
    public ResponseEntity<List<ScheduleResponseDTO>> getAllSchedulesByTrip(@RequestParam int tripId) {
        return ResponseEntity.ok().body(scheduleMapper.toDtoList(scheduleService.getAllSchedulesByTrip(tripId)));
    }

    @PostMapping()
    public ResponseEntity<List<ScheduleResponseDTO>> createFullScheduleByTrip(
            @RequestParam Integer tripId,
            @RequestParam TravelMode travelMode,
            @RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm") LocalTime startTime) {
        List<Schedule> fullScheduleByTrip = scheduleService.createFullScheduleByTrip(tripId, startTime, travelMode);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleMapper.toDtoList(fullScheduleByTrip));
    }

    @PutMapping("/reset-times")
    public ResponseEntity<Void> resetScheduleTimes(@RequestParam int tripId) {
        scheduleService.resetStartAndEndTimesByTripId(tripId);
        return ResponseEntity.noContent().build();
    }
}
