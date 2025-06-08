package com.example.backend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String city;

    @ManyToOne()
    @JoinColumn(name = "userId")
    User user;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    List<Schedule> scheduleLines;

    public Trip(Trip trip) {
        if (trip!=null) {
            this.name = trip.name;
            this.city = trip.city;
            this.user = trip.user;
            this.scheduleLines = getListOfClonedSchedules(trip);
        }
    }

    public Trip clone() {
        return new Trip(this);
    }

    private List<Schedule> getListOfClonedSchedules(Trip trip) {
        ArrayList<Schedule> listOfClonedSchedules = new ArrayList<>();
        trip.scheduleLines.forEach(schedule -> {

            Schedule cloned = schedule.clone();

            if (cloned != null) {
                cloned.setTrip(this);
                listOfClonedSchedules.add(cloned);
            }
        });
        return listOfClonedSchedules;
    }
}