package com.example.backend.service.google.place;

import com.example.backend.dto.google.GooglePlaceDTO;
import com.example.backend.dto.google.GoogleReviewDTO;
import com.example.backend.model.entities.Location;
import com.example.backend.model.enums.PlaceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleApiPlaceServiceImpl implements GoogleApiPlaceService {

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    private ObjectMapper objectMapper;

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";
    private static final String NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s&radius=%d&type=%s&key=%s";
    private static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=name,rating,reviews,formatted_address,geometry,opening_hours&key=%s";


    public Location getCoordinates(String address) {
        String url = String.format(GEOCODE_URL, URLEncoder.encode(address, StandardCharsets.UTF_8), apiKey);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode results = root.path("results");
            if (!results.isArray() || results.isEmpty()) {
                throw new RuntimeException("City not found: " + address);
            }

            JsonNode location = results.get(0).path("geometry").path("location");
            double lat = location.path("lat").asDouble();
            double lng = location.path("lng").asDouble();

            return new Location(lat, lng);
        } catch (Exception e) {
            throw new RuntimeException("Failed to geocode address", e);
        }
    }

    @Override
    public List<GooglePlaceDTO> getDetailedPlaces(String city, String categoryType, int radius) {
        List<GooglePlaceDTO> places = new ArrayList<>();
        categoryType = categoryType.toLowerCase();
        Location location = getCoordinates(city);
        String coordinates = location.getLatitude() + "," + location.getLongitude();
        String url = String.format(NEARBY_SEARCH_URL, coordinates, radius, categoryType, apiKey);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode results = objectMapper.readTree(response.body()).path("results");

            for (JsonNode result : results) {
                String placeId = result.path("place_id").asText();
                GooglePlaceDTO place = getPlaceDetails(placeId, categoryType);
                if (place != null) places.add(place);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch detailed places", e);
        }
        places.sort((place1, place2) ->
                Double.compare(place2.getRating() != null ? place2.getRating() : 0.0,
                        place1.getRating() != null ? place1.getRating() : 0.0)
        );
        return places;
    }


    private GooglePlaceDTO getPlaceDetails(String placeId, String categoryType) {
        String url = String.format(PLACE_DETAILS_URL, placeId, apiKey);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return parsePlaceFromResponse(response.body(), categoryType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch details for place_id: " + placeId);
        }
    }

    private GooglePlaceDTO parsePlaceFromResponse(String responseBody, String category) throws JsonProcessingException {
        JsonNode result = objectMapper.readTree(responseBody).path("result");

        String name = result.path("name").asText();
        String description = result.path("formatted_address").asText("No description");
        double latitude = result.path("geometry").path("location").path("lat").asDouble();
        double longitude = result.path("geometry").path("location").path("lng").asDouble();
        double rating = result.path("rating").asDouble(0.0);

        LocalTime openingTime = null;
        LocalTime closingTime = null;
        JsonNode hours = result.path("opening_hours").path("periods");
        if (hours.isArray() && hours.size() > 0) {
            JsonNode today = hours.get(0);
            String openStr = today.path("open").path("time").asText();
            String closeStr = today.path("close").path("time").asText();
            if (!openStr.isEmpty() && !closeStr.isEmpty()) {
                openingTime = LocalTime.of(
                        Integer.parseInt(openStr.substring(0, 2)),
                        Integer.parseInt(openStr.substring(2)));
                closingTime = LocalTime.of(
                        Integer.parseInt(closeStr.substring(0, 2)),
                        Integer.parseInt(closeStr.substring(2)));
            }
        }

        List<GoogleReviewDTO> reviews = new ArrayList<>();
        for (JsonNode r : result.path("reviews")) {
            GoogleReviewDTO review = GoogleReviewDTO.builder()
                    .rating(r.path("rating").asInt())
                    .comment(r.path("text").asText(""))
                    .timeDescription(r.path("relative_time_description").asText(""))
                    .build();
            reviews.add(review);
        }

        return GooglePlaceDTO.builder().name(name)
                .description(description)
                .placeType(PlaceType.valueOf(category.toUpperCase()))
                .rating(rating)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .latitude(latitude)
                .longitude(longitude)
                .reviews(reviews)
                .build();
    }
}
