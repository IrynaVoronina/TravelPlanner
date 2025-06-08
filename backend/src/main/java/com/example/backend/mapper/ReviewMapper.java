package com.example.backend.mapper;

import com.example.backend.dto.review.ReviewRequestDTO;
import com.example.backend.dto.review.ReviewResponseDTO;
import com.example.backend.model.entities.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toModel(ReviewRequestDTO dto);

    ReviewResponseDTO toDto(Review review);

    List<ReviewResponseDTO> toDtoList(List<Review> reviews);
}


