package com.example.backend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    int rating;
    String comment;
    String timeDescription;

    @ManyToOne()
    @JoinColumn(name = "placeId", referencedColumnName = "activityId")
    Place place;
}
