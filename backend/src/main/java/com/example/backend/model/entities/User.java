package com.example.backend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String username;
    String password;
    String roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Trip> trip;
}
