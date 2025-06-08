package com.example.backend.controller;

import com.example.backend.dto.activity.place.PlaceResponseDTO;
import com.example.backend.dto.google.GooglePlaceDTO;
import com.example.backend.mapper.ActivityMapper;
import com.example.backend.model.entities.Place;
import com.example.backend.model.entities.Schedule;
import com.example.backend.service.activity.ActivityService;
import com.example.backend.service.google.place.GoogleApiPlaceService;
import com.example.backend.service.schedule.ScheduleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/api/v1/activities/places")
public class PlaceController {
    @Qualifier("placeService")
    private final ActivityService placeService;
    private final ActivityMapper activityMapper;
    private final GoogleApiPlaceService googleApiPlaceService;
    private final ScheduleService scheduleService;

    public PlaceController(ActivityService placeService,
                           ActivityMapper activityMapper,
                           GoogleApiPlaceService googleApiPlaceService,
                           ScheduleService scheduleService) {
        this.placeService = placeService;
        this.activityMapper = activityMapper;
        this.googleApiPlaceService = googleApiPlaceService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<GooglePlaceDTO>> getNearby(
            @RequestParam String city,
            @RequestParam(defaultValue = "tourist_attraction") String type,
            @RequestParam(defaultValue = "3000000") int radius) {
        return ResponseEntity.ok().body(googleApiPlaceService.getDetailedPlaces(city, type, radius));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponseDTO> getPlaceById(@PathVariable int id) {
        Place place = (Place) placeService.getActivityById(id);
        return ResponseEntity.ok().body(activityMapper.toPlaceDto(place));
    }

    @GetMapping()
    public ResponseEntity<List<PlaceResponseDTO>> getAllPlacesByTrip(@RequestParam Integer tripId) {
        List<Place> places = placeService.getActivities(tripId);
        return ResponseEntity.ok().body(activityMapper.toPlaceDtoList(places));
    }

    @PostMapping()
    public ResponseEntity<PlaceResponseDTO> addPlaceToTrip(@RequestBody @Valid GooglePlaceDTO selectedPlace,
                                                           @RequestParam Integer tripId) {
        Place placeModel = activityMapper.toPlaceModel(selectedPlace);
        Place addedPlace = (Place) placeService.addActivity(placeModel, tripId);
        return ResponseEntity.ok(activityMapper.toPlaceDto(addedPlace));
    }

    @PutMapping()
    public ResponseEntity<PlaceResponseDTO> updatePlaceVisitDuration(@RequestParam Integer placeId,
                                                                     @RequestParam @NotNull int visitDuration) {
        Place place = (Place) placeService.getActivityById(placeId);
        place.setVisitDuration(visitDuration);
        Place updatedPlace = (Place) placeService.updateActivity(place);
        return ResponseEntity.ok(activityMapper.toPlaceDto(updatedPlace));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deletePlace(@RequestParam Integer tripId,
                                            @RequestParam Integer placeId) {
        List<Schedule> schedules = scheduleService.findByTripIdAndActivityId(tripId, placeId);
        placeService.deleteActivity(schedules, placeId);
        return ResponseEntity.noContent().build();
    }
}
