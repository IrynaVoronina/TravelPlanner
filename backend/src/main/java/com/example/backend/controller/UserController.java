package com.example.backend.controller;

/*
import com.example.backend.dto.user.UserRequestDTO;
import com.example.backend.dto.user.UserResponseDTO;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entities.User;
import com.example.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/users")
public class UserController {

    UserService service;
    UserMapper mapper;
    PasswordEncoder passwordEncoder;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome!!!!!!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "Admin!!!!!!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String user() {
        return "User!!!!!!";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok().body(mapper.toDtoList(service.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(mapper.toDto(service.getById(id)));
    }

    @PostMapping("sign-up")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO createDto) {
        createDto.setPassword(passwordEncoder.encode(createDto.getPassword()));
        User userToCreate = mapper.toModel(createDto);
        User createdUser = service.create(userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(createdUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Integer id,
                                                    @RequestBody @Valid UserRequestDTO updateDto) {
        User user = mapper.toModel(updateDto);
        user.setId(id);
        User updatedUser = service.update(user);
        return ResponseEntity.ok().body(mapper.toDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/searchByLogin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> searchByLastName(@RequestParam("query") String query) {
        return ResponseEntity.ok().body(mapper.toDtoList(service.getByLogin(query)));
    }

}

 */