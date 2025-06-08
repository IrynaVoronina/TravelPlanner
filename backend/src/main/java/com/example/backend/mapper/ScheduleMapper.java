package com.example.backend.mapper;

import com.example.backend.dto.schedule.ScheduleRequestDTO;
import com.example.backend.dto.schedule.ScheduleResponseDTO;
import com.example.backend.model.entities.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ActivityMapper.class)
public interface ScheduleMapper {

    Schedule toModel(ScheduleRequestDTO dto);

    ScheduleResponseDTO toDto(Schedule schedule);

    List<ScheduleResponseDTO> toDtoList(List<Schedule> schedules);
}
