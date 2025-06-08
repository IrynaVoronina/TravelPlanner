package com.example.backend.dto.google;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleReviewDTO {
    Integer rating;
    String comment;
    String timeDescription;
}