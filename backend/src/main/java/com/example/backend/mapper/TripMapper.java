package com.example.backend.mapper;

import com.example.backend.dto.trip.TripRequestDTO;
import com.example.backend.dto.trip.TripResponseDTO;
import com.example.backend.model.entities.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    Trip toModel(TripRequestDTO dto);

    TripResponseDTO toDto(Trip trip);

    List<TripResponseDTO> toDtoList(List<Trip> trips);
}
