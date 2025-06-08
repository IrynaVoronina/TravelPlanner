package com.example.backend.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PrimaryKeyJoinColumn(name = "activityId")
@Table(name = "meal_breaks")
public class MealBreak extends Activity{

    LocalTime startTime;
    LocalTime endTime;
    int duration;

    public MealBreak(MealBreak mealBreak) {
        if (mealBreak!=null) {
            super.name = mealBreak.name;
            super.description = mealBreak.description;
            super.location = mealBreak.location;
            this.startTime = mealBreak.startTime;
            this.endTime = mealBreak.endTime;
            this.duration = mealBreak.duration;
        }
    }

    public Activity clone() {
        return new MealBreak(this);
    }
}
