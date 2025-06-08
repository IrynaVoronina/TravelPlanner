package com.example.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDTO {

    @NotBlank(message = "Username must be specified")
    @Size(max = 30, message = "Username must have at most 30 characters")
    String username;
    String password;
    String roles;
}
