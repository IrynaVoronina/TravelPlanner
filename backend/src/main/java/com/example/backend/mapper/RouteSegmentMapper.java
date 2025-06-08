package com.example.backend.mapper;

import com.example.backend.dto.google.GoogleRouteSegmentDTO;
import com.example.backend.dto.route.RouteSegmentResponseDTO;
import com.example.backend.model.entities.RouteSegment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteSegmentMapper {

    @Mapping(target = "route", ignore = true)
    RouteSegment toModel(GoogleRouteSegmentDTO dto);

    List<RouteSegment> toModelList(List<GoogleRouteSegmentDTO> dtoList);

    RouteSegmentResponseDTO toDto(RouteSegment routeSegment);

    List<RouteSegmentResponseDTO> toDtoList(List<RouteSegment> routeSegmentList);
}
