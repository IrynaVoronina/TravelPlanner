package com.example.backend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalTime startTime;
    LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "tripId")
    Trip trip;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "activityId")
    Activity activity;

    public Schedule(Schedule schedule) {
        if (schedule!=null) {
            this.startTime = schedule.startTime;
            this.endTime = schedule.endTime;
            this.activity = schedule.activity.clone();
        }
    }

    public Schedule clone() {
        return new Schedule(this);
    }
}


