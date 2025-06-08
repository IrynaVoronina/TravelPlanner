package com.example.backend.dto.user;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO {
    Integer id;
    String username;
    String password;
    String roles;
}
