package com.example.backend.dto.review;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequestDTO {

    @NotNull(message = "Rating must be specified")
    @Min(1)
    @Max(5)
    Integer rating;

    @NotBlank(message = "Comment must be specified")
    @Size(max = 200, message = "Comment must have at most 200 characters")
    String comment;

}
