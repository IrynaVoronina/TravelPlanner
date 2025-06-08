package com.example.backend.service.google.route;

import com.example.backend.dto.google.GoogleRouteDTO;
import com.example.backend.dto.google.GoogleRouteSegmentDTO;
import com.example.backend.model.entities.Location;
import com.example.backend.model.enums.TravelMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleApiRouteServiceImpl implements GoogleApiRouteService {

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    private ObjectMapper objectMapper;

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String ROUTES_API_URL = "https://routes.googleapis.com/directions/v2:computeRoutes";


    public GoogleRouteDTO getRoute(Location origin, Location destination, TravelMode travelMode) {
        try {
            HttpRequest request = buildHttpRequest(origin, destination, travelMode);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return parseRouteFromResponse(response.body(), origin, destination);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to get route from Google API", e);
        }
    }

    private HttpRequest buildHttpRequest(Location origin, Location destination, TravelMode travelMode) throws JsonProcessingException {

        Map<String, Object> requestBody = buildRequestBody(origin, destination, travelMode);
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        return HttpRequest.newBuilder()
                .uri(URI.create(ROUTES_API_URL))
                .header("Content-Type", "application/json")
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", "routes.legs.steps")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }

    private Map<String, Object> buildRequestBody(Location origin, Location destination, TravelMode travelMode) {
        Map<String, Object> originMap = Map.of("location", Map.of(
                "latLng", Map.of("latitude", origin.getLatitude(), "longitude", origin.getLongitude())));
        Map<String, Object> destinationMap = Map.of("location", Map.of(
                "latLng", Map.of("latitude", destination.getLatitude(), "longitude", destination.getLongitude())));

        return Map.of(
                "origin", originMap,
                "destination", destinationMap,
                "travelMode", travelMode.toString()
        );
    }

    private GoogleRouteDTO parseRouteFromResponse(String responseBody, Location origin, Location destination) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(responseBody);
        List<GoogleRouteSegmentDTO> segments = new ArrayList<>();
        int totalDuration = 0;

        JsonNode steps = root.path("routes").get(0).path("legs").get(0).path("steps");
        for (JsonNode step : steps) {
            Location start = extractLocation(step.path("startLocation").path("latLng"));
            Location end = extractLocation(step.path("endLocation").path("latLng"));

            String durationStr = step.path("staticDuration").asText();
            int duration = Integer.parseInt(durationStr.replace("s", ""));

            int distanceMeters = step.path("distanceMeters").asInt();
            TravelMode travelMode = TravelMode.valueOf(step.path("travelMode").asText());


            GoogleRouteSegmentDTO segment = GoogleRouteSegmentDTO.builder()
                    .startPoint(start)
                    .endPoint(end)
                    .staticDuration(duration)
                    .distanceMeters(distanceMeters)
                    .travelMode(travelMode)
                    .build();

            segments.add(segment);
            totalDuration += duration;
        }

        return GoogleRouteDTO.builder()
                .startPlace(origin)
                .endPlace(destination)
                .segments(segments)
                .totalDuration(totalDuration)
                .build();
    }

    private Location extractLocation(JsonNode locationNode) {
        double lat = locationNode.path("latitude").asDouble();
        double lng = locationNode.path("longitude").asDouble();
        return new Location(lat, lng);
    }
}

