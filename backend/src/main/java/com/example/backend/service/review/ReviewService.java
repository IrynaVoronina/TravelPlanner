package com.example.backend.service.review;

import com.example.backend.model.entities.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getAllByPlaceId(int placeId);

    Review getById(Integer id);

    Review create(Review review);

    Review update(Review review);

    void delete(Integer id);
}
