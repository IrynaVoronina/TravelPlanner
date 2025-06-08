package com.example.backend.service.review;

import com.example.backend.model.entities.Review;
import com.example.backend.repository.ReviewRepository;
import com.example.backend.validation.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getAllByPlaceId(int placeId) {
        return reviewRepository.findAllByPlaceId(placeId);
    }

    @Override
    public Review getById(Integer id) {
        return getOrElseThrow(id);
    }

    private Review getOrElseThrow(Integer id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Review with id %s does not exist", id)));
    }

    @Override
    public Review create(Review review) {
        review.setTimeDescription(LocalTime.now().toString());
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        getOrElseThrow(review.getId());
        return reviewRepository.save(review);
    }

    @Override
    public void delete(Integer id) {
        getOrElseThrow(id);
        reviewRepository.deleteById(id);
    }
}
