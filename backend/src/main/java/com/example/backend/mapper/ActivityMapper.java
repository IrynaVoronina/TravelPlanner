package com.example.backend.mapper;

import com.example.backend.dto.activity.ActivityResponseDTO;
import com.example.backend.dto.activity.accommodation.AccommodationRequestDTO;
import com.example.backend.dto.activity.accommodation.AccommodationResponseDTO;
import com.example.backend.dto.activity.meal.MealBreakRequestDTO;
import com.example.backend.dto.activity.meal.MealBreakResponseDTO;
import com.example.backend.dto.activity.place.PlaceResponseDTO;
import com.example.backend.dto.google.GooglePlaceDTO;
import com.example.backend.model.entities.Accommodation;
import com.example.backend.model.entities.Activity;
import com.example.backend.model.entities.MealBreak;
import com.example.backend.model.entities.Place;
import com.example.backend.model.enums.PlaceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface ActivityMapper {

    @Mapping(source = "placeType", target = "placeType")
    @Mapping(source = "openingTime", target = "openingTime", dateFormat = "HH:mm")
    @Mapping(source = "closingTime", target = "closingTime", dateFormat = "HH:mm")
    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.longitude", target = "longitude")
    @Mapping(source = "reviews", target = "reviews")
    PlaceResponseDTO toPlaceDto(Place place);

    List<PlaceResponseDTO> toPlaceDtoList(List<Place> places);

    @Mapping(source = "openingTime", target = "openingTime", dateFormat = "HH:mm")
    @Mapping(source = "closingTime", target = "closingTime", dateFormat = "HH:mm")
    @Mapping(source = "latitude", target = "location.latitude")
    @Mapping(source = "longitude", target = "location.longitude")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "visitDuration", expression = "java(getVisitDuration(placeSelectedDTO.getPlaceType()))")
    Place toPlaceModel(GooglePlaceDTO placeSelectedDTO);

    default int getVisitDuration(PlaceType placeType) {
        return placeType != null ? placeType.getDefaultVisitDuration() : 60;
    }

    Accommodation toAccommodationModel(AccommodationRequestDTO accommodationRequestDTO);

    @Mapping(source = "accommodationType", target = "accommodationType")
    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.longitude", target = "longitude")
    AccommodationResponseDTO toAccommodationDto(Accommodation accommodation);

    List<AccommodationResponseDTO> toAccommodationDtoList(List<Accommodation> accommodation);

    @Mapping(target = "name", expression = "java(dto.getName().name())")
    @Mapping(target = "description", expression = "java(getMealBreakDescription(dto))")
    @Mapping(source = "startTime", target = "startTime", dateFormat = "HH:mm")
    @Mapping(source = "endTime", target = "endTime", dateFormat = "HH:mm")
    @Mapping(target = "duration", ignore = true)
    MealBreak toMealBreakModel(MealBreakRequestDTO dto);

    default String getMealBreakDescription(MealBreakRequestDTO dto) {
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            return dto.getDescription();
        }
        return dto.getName().getDescription();
    }
    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.longitude", target = "longitude")
    MealBreakResponseDTO toMealBreakDto(MealBreak mealBreak);

    List<MealBreakResponseDTO> toMealBreakDtoList(List<MealBreak> mealBreak);

    ActivityResponseDTO toActivityDto(Activity activity);
}
