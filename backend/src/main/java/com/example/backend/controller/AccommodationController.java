package com.example.backend.controller;

import com.example.backend.dto.activity.accommodation.AccommodationRequestDTO;
import com.example.backend.dto.activity.accommodation.AccommodationResponseDTO;
import com.example.backend.mapper.ActivityMapper;
import com.example.backend.model.entities.Accommodation;
import com.example.backend.model.entities.Location;
import com.example.backend.model.entities.Schedule;
import com.example.backend.service.activity.ActivityService;
import com.example.backend.service.google.place.GoogleApiPlaceService;
import com.example.backend.service.schedule.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/api/v1/activities/accommodations")
public class AccommodationController {
    @Qualifier("accommodationService")
    private final ActivityService accommodationService;
    private final ActivityMapper activityMapper;
    private final ScheduleService scheduleService;
    private final GoogleApiPlaceService googleApiPlaceService;

    public AccommodationController(ActivityService accommodationService,
                                   ActivityMapper activityMapper,
                                   ScheduleService scheduleService,
                                   GoogleApiPlaceService googleApiPlaceService) {
        this.accommodationService = accommodationService;
        this.activityMapper = activityMapper;
        this.scheduleService = scheduleService;
        this.googleApiPlaceService = googleApiPlaceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationResponseDTO> getAccommodationById(@PathVariable int id) {
        Accommodation accommodation = (Accommodation) accommodationService.getActivityById(id);
        return ResponseEntity.ok().body(activityMapper.toAccommodationDto(accommodation));
    }

    @GetMapping()
    public ResponseEntity<List<AccommodationResponseDTO>> getAllAccommodationsByTrip(@RequestParam Integer tripId) {
        List<Accommodation> accommodations = accommodationService.getActivities(tripId);
        return ResponseEntity.ok().body(activityMapper.toAccommodationDtoList(accommodations));
    }

    @PostMapping()
    public ResponseEntity<AccommodationResponseDTO> addAccommodationToTrip(@RequestParam Integer tripId,
                                                                           @RequestBody @Valid AccommodationRequestDTO accommodationDTO) {
        Location location = googleApiPlaceService.getCoordinates(accommodationDTO.getDescription());
        Accommodation accommodationModel = activityMapper.toAccommodationModel(accommodationDTO);
        accommodationModel.setLocation(location);
        Accommodation addedAccommodation = (Accommodation) accommodationService.addActivity(accommodationModel, tripId);
        return ResponseEntity.ok(activityMapper.toAccommodationDto(addedAccommodation));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AccommodationResponseDTO> updateAccommodation(@PathVariable int id,
                                                                        @RequestBody @Valid AccommodationRequestDTO accommodationDTO) {
        Location location = googleApiPlaceService.getCoordinates(accommodationDTO.getDescription());
        Accommodation accommodationModel = activityMapper.toAccommodationModel(accommodationDTO);
        accommodationModel.setId(id);
        accommodationModel.setLocation(location);
        Accommodation updatedAccommodation = (Accommodation) accommodationService.updateActivity(accommodationModel);
        return ResponseEntity.ok(activityMapper.toAccommodationDto(updatedAccommodation));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAccommodation(@RequestParam Integer tripId,
                                                    @RequestParam Integer accommodationId) {
        List<Schedule> schedules = scheduleService.findByTripIdAndActivityId(tripId, accommodationId);
        accommodationService.deleteActivity(schedules, accommodationId);
        return ResponseEntity.noContent().build();
    }
}