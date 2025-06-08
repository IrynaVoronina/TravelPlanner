package com.example.backend.dto.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponseDTO {
    Integer id;
    Integer rating;
    String comment;
    String timeDescription;
}
