package com.example.backend.model.entities;

import com.example.backend.model.enums.AccommodationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PrimaryKeyJoinColumn(name = "activityId")
@Table(name = "accomodations")
public class Accommodation extends Activity{

    @Enumerated(EnumType.STRING)
    AccommodationType accommodationType;

    double pricePerNight;
    int stars;

    boolean wifiAvailable;
    boolean parkingAvailable;
    boolean petFriendly;

    public Accommodation(Accommodation accommodation) {
        if (accommodation!=null) {
            super.name = accommodation.name;
            super.description = accommodation.description;
            super.location = accommodation.location;
            this.accommodationType = accommodation.accommodationType;
            this.pricePerNight = accommodation.pricePerNight;
            this.stars = accommodation.stars;
            this.wifiAvailable = accommodation.wifiAvailable;
            this.parkingAvailable = accommodation.parkingAvailable;
            this.petFriendly = accommodation.petFriendly;
        }
    }

    public Activity clone() {
        return new Accommodation(this);
    }
}
