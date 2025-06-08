package com.example.backend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne()
    @JoinColumn(name = "tripId")
    Trip trip;

    @ManyToOne
    Activity start;

    @ManyToOne
    Activity end;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    List<RouteSegment> segments;

    int totalDuration;

}