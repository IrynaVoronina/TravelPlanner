package com.example.backend.dto.trip;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripResponseDTO {

    Integer id;
    String name;
    String city;

}
