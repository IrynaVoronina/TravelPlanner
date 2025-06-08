package com.example.backend.controller;

import com.example.backend.dto.activity.meal.MealBreakRequestDTO;
import com.example.backend.dto.activity.meal.MealBreakResponseDTO;
import com.example.backend.mapper.ActivityMapper;
import com.example.backend.model.entities.Location;
import com.example.backend.model.entities.MealBreak;
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
@RequestMapping("/api/v1/activities/mealBreaks")
public class MealBreakController {
    @Qualifier("mealBreakService")
    private final ActivityService mealBreakService;
    private final ActivityMapper activityMapper;
    private final ScheduleService scheduleService;
    private final GoogleApiPlaceService googleApiPlaceService;

    public MealBreakController(ActivityService mealBreakService,
                               ActivityMapper activityMapper,
                               ScheduleService scheduleService,
                               GoogleApiPlaceService googleApiPlaceService) {
        this.mealBreakService = mealBreakService;
        this.activityMapper = activityMapper;
        this.scheduleService = scheduleService;
        this.googleApiPlaceService = googleApiPlaceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealBreakResponseDTO> getMealBreakById(@PathVariable int id) {
        MealBreak mealBreak = (MealBreak) mealBreakService.getActivityById(id);
        return ResponseEntity.ok().body(activityMapper.toMealBreakDto(mealBreak));
    }

    @GetMapping()
    public ResponseEntity<List<MealBreakResponseDTO>> getAllMealBreaksByTrip(@RequestParam Integer tripId) {
        List<MealBreak> mealBreaks = mealBreakService.getActivities(tripId);
        return ResponseEntity.ok().body(activityMapper.toMealBreakDtoList(mealBreaks));
    }

    @PostMapping()
    public ResponseEntity<MealBreakResponseDTO> addMealBreakToTrip(@RequestParam Integer tripId,
                                                                   @RequestBody @Valid MealBreakRequestDTO mealBreakDTO) {
        MealBreak mealBreakModel = activityMapper.toMealBreakModel(mealBreakDTO);
        MealBreak addedMealBreak = (MealBreak) mealBreakService.addActivity(mealBreakModel, tripId);
        return ResponseEntity.ok(activityMapper.toMealBreakDto(addedMealBreak));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MealBreakResponseDTO> updateMealBreak(@PathVariable int id,
                                                                        @RequestBody @Valid MealBreakRequestDTO mealBreakDTO) {

        MealBreak mealBreakModel = activityMapper.toMealBreakModel(mealBreakDTO);
        mealBreakModel.setId(id);
        MealBreak updatedMealBreak = (MealBreak) mealBreakService.updateActivity(mealBreakModel);
        return ResponseEntity.ok(activityMapper.toMealBreakDto(updatedMealBreak));
    }
    
    @DeleteMapping()
    public ResponseEntity<Void> deleteMealBreak(@RequestParam Integer tripId,
                                                    @RequestParam Integer mealBreakId) {
        List<Schedule> schedules = scheduleService.findByTripIdAndActivityId(tripId, mealBreakId);
        mealBreakService.deleteActivity(schedules, mealBreakId);
        return ResponseEntity.noContent().build();
    }
}