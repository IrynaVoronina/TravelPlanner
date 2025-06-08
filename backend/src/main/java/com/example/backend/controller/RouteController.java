package com.example.backend.controller;

import com.example.backend.dto.route.RouteResponseDTO;
import com.example.backend.mapper.RouteMapper;
import com.example.backend.model.entities.Route;
import com.example.backend.service.route.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/api/v1/routes")
public class RouteController {

    private final RouteService routeService;
    private final RouteMapper routeMapper;

    @GetMapping()
    public ResponseEntity<List<RouteResponseDTO>> getAllByTrip(@RequestParam int tripId) {
        List<Route> routes = routeService.getAllByTrip(tripId);
        return ResponseEntity.ok(routeMapper.toDtoList(routes));
    }
}