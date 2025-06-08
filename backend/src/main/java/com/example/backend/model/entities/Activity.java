package com.example.backend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activities")
public abstract class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "activity_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "activity_longitude"))
    })
    Location location;

    public abstract Activity clone();
}
