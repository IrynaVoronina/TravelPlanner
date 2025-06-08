package com.example.backend.service.route;

import com.example.backend.dto.google.GoogleRouteDTO;
import com.example.backend.mapper.RouteMapper;
import com.example.backend.model.entities.*;
import com.example.backend.model.enums.TravelMode;
import com.example.backend.repository.RouteRepository;
import com.example.backend.service.google.route.GoogleApiRouteService;
import com.example.backend.service.trip.TripService;
import com.example.backend.validation.RouteSequenceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RouteServiceImpl implements RouteService {
    GoogleApiRouteService googleApiRouteService;
    RouteRepository routeRepository;
    RouteMapper routeMapper;
    TripService tripService;

    @Override
    public List<Route> getAllByTrip(int tripId) {
        return routeRepository.findAllByTripId(tripId);
    }

    @Override
    @Transactional
    public List<Route> findOptimalRouteSequence(List<Place> places,
                                                Accommodation accommodation,
                                                List<MealBreak> mealBreaks,
                                                int tripId,
                                                LocalTime startTime,
                                                TravelMode travelMode) {

        List<Route> allRoutes = findAllRoutes(places, accommodation, tripId, travelMode);

        List<Place> attractions = new ArrayList<>(places);
        List<List<Place>> permutations = generatePermutations(attractions);

        List<Route> bestRouteSequence = null;
        int minTotalDuration = Integer.MAX_VALUE;

        for (List<Place> permutation : permutations) {
            List<Activity> fullRoute = new ArrayList<>();
            if (accommodation != null) fullRoute.add(accommodation);
            fullRoute.addAll(permutation);
            if (accommodation != null) fullRoute.add(accommodation);

            List<Route> routeSequence = buildRouteSequence(fullRoute, allRoutes);
            if (routeSequence == null) continue;

            LocalTime currentTime = startTime;
            boolean isValid = true;
            int totalDuration = 0;
            int segmentIndex = 0;

            for (int i = 0; i < fullRoute.size(); i++) {
                Activity current = fullRoute.get(i);

                if (i > 0) {
                    Route route = routeSequence.get(segmentIndex++);
                    currentTime = currentTime.plusSeconds(route.getTotalDuration());
                    totalDuration += route.getTotalDuration();
                }

                for (MealBreak mealBreak : mealBreaks) {
                    if (!currentTime.isBefore(mealBreak.getStartTime()) &&
                            !currentTime.isAfter(mealBreak.getEndTime().minusHours(mealBreak.getDuration()))) {
                        currentTime = mealBreak.getStartTime().plusHours(mealBreak.getDuration());
                        break;
                    }
                }

                if (current instanceof Place) {
                    Place place = (Place) current;

                    if (!isPlaceAvailable(place, currentTime)) {
                        isValid = false;
                        break;
                    }

                    currentTime = currentTime.plusMinutes(place.getVisitDuration());
                }
            }

            if (!isValid) continue;

            if (totalDuration < minTotalDuration) {
                minTotalDuration = totalDuration;
                bestRouteSequence = routeSequence;
            }
        }

        if (bestRouteSequence == null) {
            throw new RouteSequenceException("Unable to build route");
        }

        return saveRouteSequence(bestRouteSequence, tripId);
    }


    @Override
    public List<Route> saveRouteSequence(List<Route> routes, int tripId) {
        if (routes.isEmpty()) return Collections.emptyList();

        routeRepository.deleteByTripId(tripId);

        for (Route route : routes) {
            if (route.getSegments() != null) {
                for (RouteSegment segment : route.getSegments()) {
                    segment.setRoute(route);
                }
            }
        }
        return routeRepository.saveAll(routes);
    }

    private List<Route> findAllRoutes(List<Place> places, Accommodation accommodation, int tripId, TravelMode travelMode) {
        List<Activity> allActivities = new ArrayList<>(places);
        if (accommodation != null) allActivities.add(accommodation);
        return generateRoutes(allActivities, tripId, travelMode);
    }



    private List<Route> generateRoutes(List<Activity> activities, int tripId, TravelMode travelMode) {
        List<Route> allRoutes = new ArrayList<>();

        for (int i = 0; i < activities.size(); ++i) {
            for (int j = 0; j < activities.size(); ++j) {
                if (i != j) {
                    Activity origin = activities.get(i);
                    Activity destination = activities.get(j);

                    GoogleRouteDTO googleRoute = googleApiRouteService.getRoute(origin.getLocation(), destination.getLocation(), travelMode);
                    System.out.println("\n\n ========= googleRoute ==============" + googleRoute);

                    Route route = buildRoute(tripId, origin, destination, googleRoute);
                    allRoutes.add(route);
                }
            }
        }
        return allRoutes;
    }


    private Route buildRoute(int tripId, Activity origin, Activity destination, GoogleRouteDTO googleRouteDto) {
        Trip trip = tripService.getById(tripId);
        return routeMapper.toModel(googleRouteDto, trip, origin, destination);
    }

    private List<List<Place>> generatePermutations(List<Place> activities) {
        List<List<Place>> result = new ArrayList<>();
        this.permute(activities, 0, result);
        return result;
    }

    private void permute(List<Place> places, int start, List<List<Place>> result) {
        if (start == places.size() - 1) {
            result.add(new ArrayList<>(places));
            return;
        }

        for (int i = start; i < places.size(); i++) {
            Collections.swap(places, i, start);
            permute(places, start + 1, result);
            Collections.swap(places, i, start);
        }
    }

    private List<Route> buildRouteSequence(List<Activity> permutation, List<Route> allRoutes) {
        List<Route> sequence = new ArrayList<>();

        for (int i = 0; i < permutation.size() - 1; i++) {
            Activity from = permutation.get(i);
            Activity to = permutation.get(i + 1);

            Optional<Route> routeOpt = allRoutes.stream()
                    .filter(r -> r.getStart().getId().equals(from.getId())
                            && r.getEnd().getId().equals(to.getId()))
                    .findFirst();

            if (routeOpt.isEmpty()) {
                return null;
            }

            sequence.add(routeOpt.get());
        }

        return sequence;
    }


    private boolean isPlaceAvailable(Place place, LocalTime arrivalTime) {
        LocalTime openingTime = place.getOpeningTime();
        LocalTime closingTime = place.getClosingTime();
        int visitMinutes = place.getVisitDuration();
        LocalTime departureTime = arrivalTime.plusMinutes(visitMinutes);

        boolean afterOpen = openingTime == null || !arrivalTime.isBefore(openingTime);
        boolean beforeClose = closingTime == null || !departureTime.isAfter(closingTime);

        return afterOpen && beforeClose;
    }
}