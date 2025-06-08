package com.example.backend.dto.activity.accommodation;

import com.example.backend.model.enums.AccommodationType;
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
public class AccommodationRequestDTO {

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Description is required")
    String description;

    @NotNull(message = "Accommodation type is required")
    AccommodationType accommodationType;

    @Positive(message = "Price per night must be greater than 0")
    double pricePerNight;

    @Min(value = 1, message = "Stars must be at least 1")
    @Max(value = 5, message = "Stars cannot exceed 5")
    int stars;

    boolean wifiAvailable;
    boolean parkingAvailable;
    boolean petFriendly;
}
