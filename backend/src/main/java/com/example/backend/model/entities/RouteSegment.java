package com.example.backend.model.entities;

import com.example.backend.model.enums.TravelMode;
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
@Table(name = "route_segments")
public class RouteSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne()
    @JoinColumn(name = "routeId")
    Route route;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "start_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "start_longitude"))
    })
    Location startPoint;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "end_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "edt_longitude"))
    })
    Location endPoint;

    @Enumerated(EnumType.STRING)
    TravelMode travelMode;

    int staticDuration;
    int distanceMeters;
}
