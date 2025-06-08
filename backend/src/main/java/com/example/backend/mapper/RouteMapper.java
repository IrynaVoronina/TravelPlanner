package com.example.backend.mapper;

import com.example.backend.dto.google.GoogleRouteDTO;
import com.example.backend.dto.route.RouteResponseDTO;
import com.example.backend.model.entities.Activity;
import com.example.backend.model.entities.Route;
import com.example.backend.model.entities.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = RouteSegmentMapper.class)
public interface RouteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", expression = "java(trip)")
    @Mapping(target = "start", expression = "java(start)")
    @Mapping(target = "end", expression = "java(end)")
    @Mapping(target = "segments", source = "googleRouteDto.segments")
    @Mapping(target = "totalDuration", source = "googleRouteDto.totalDuration")
    Route toModel(GoogleRouteDTO googleRouteDto, Trip trip, Activity start, Activity end);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "start.location", target = "startPlace")
    @Mapping(source = "end.location", target = "endPlace")
    RouteResponseDTO toDto(Route route);

    List<RouteResponseDTO> toDtoList(List<Route> routes);

}
