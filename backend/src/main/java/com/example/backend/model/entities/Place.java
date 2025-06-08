package com.example.backend.model.entities;

import com.example.backend.model.enums.PlaceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PrimaryKeyJoinColumn(name = "activityId")
@Table(name = "places")
public class Place extends Activity {

    @Enumerated(EnumType.STRING)
    PlaceType placeType;

    double rating;

    LocalTime openingTime;
    LocalTime closingTime;
    int visitDuration;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE)
    List<Review> reviews;

    public Place(Place place) {
        if (place!=null) {
            super.name = place.name;
            super.description = place.description;
            super.location = place.location;
            this.placeType = place.placeType;
            this.rating = place.rating;
            this.openingTime = place.openingTime;
            this.closingTime = place.closingTime;
            this.visitDuration = place.visitDuration;
            this.reviews = null;
        }
    }

    public Activity clone() {
        return new Place(this);
    }
}

