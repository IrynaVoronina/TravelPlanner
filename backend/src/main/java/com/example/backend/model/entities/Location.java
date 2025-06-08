package com.example.backend.model.entities;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    Double latitude;
    Double longitude;
}
