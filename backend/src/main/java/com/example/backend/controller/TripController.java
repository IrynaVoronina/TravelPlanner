package com.example.backend.controller;

import com.example.backend.dto.trip.TripRequestDTO;
import com.example.backend.dto.trip.TripResponseDTO;
import com.example.backend.mapper.TripMapper;
import com.example.backend.model.entities.Trip;
import com.example.backend.service.trip.TripService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;
    private final TripMapper tripMapper;

    @GetMapping()
    public ResponseEntity<List<TripResponseDTO>> getAll() {
        return ResponseEntity.ok().body(tripMapper.toDtoList(tripService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok().body(tripMapper.toDto(tripService.getById(id)));
    }

    @PostMapping()
    public ResponseEntity<TripResponseDTO> create(@RequestBody @Valid TripRequestDTO createDTO) {
        Trip tripToCreate = tripMapper.toModel(createDTO);
        Trip createdTrip = tripService.create(tripToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(tripMapper.toDto(createdTrip));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripResponseDTO> update(@PathVariable int id,
                                                    @RequestBody @Valid TripRequestDTO updateDTO) {
        Trip trip = tripMapper.toModel(updateDTO);
        trip.setId(id);
        Trip updated = tripService.update(trip);
        return ResponseEntity.ok().body(tripMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        tripService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clone")
    public ResponseEntity<TripResponseDTO> cloneTrip(@RequestParam int tripId) {
        return ResponseEntity.ok().body(tripMapper.toDto(tripService.cloneFullTrip(tripId)));
    }
}
