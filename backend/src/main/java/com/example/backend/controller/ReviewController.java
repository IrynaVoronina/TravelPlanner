package com.example.backend.controller;

import com.example.backend.dto.review.ReviewRequestDTO;
import com.example.backend.dto.review.ReviewResponseDTO;
import com.example.backend.mapper.ReviewMapper;
import com.example.backend.model.entities.Place;
import com.example.backend.model.entities.Review;
import com.example.backend.service.activity.ActivityService;
import com.example.backend.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    @Qualifier("placeService")
    private final ActivityService placeService;

    @GetMapping()
    public ResponseEntity<List<ReviewResponseDTO>> getAllByPlace(@RequestParam int placeId) {
        return ResponseEntity.ok().body(reviewMapper.toDtoList(reviewService.getAllByPlaceId(placeId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok().body(reviewMapper.toDto(reviewService.getById(id)));
    }

    @PostMapping()
    public ResponseEntity<ReviewResponseDTO> create(@RequestParam Integer placeId,
                                                    @RequestBody @Valid ReviewRequestDTO createDTO) {
        Review reviewToCreate = reviewMapper.toModel(createDTO);
        Place place = (Place) placeService.getActivityById(placeId);
        reviewToCreate.setPlace(place);
        Review createdReview = reviewService.create(reviewToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toDto(createdReview));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable int id,
                                                    @RequestBody @Valid ReviewRequestDTO updateDTO) {
        Review review = reviewMapper.toModel(updateDTO);
        review.setId(id);
        Review updatedReview = reviewService.update(review);
        return ResponseEntity.ok().body(reviewMapper.toDto(updatedReview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }
}



