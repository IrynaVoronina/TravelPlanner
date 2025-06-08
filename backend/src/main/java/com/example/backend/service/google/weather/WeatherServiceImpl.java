package com.example.backend.service.google.weather;

import com.example.backend.dto.google.WeatherRecommendationDTO;
import com.example.backend.model.entities.Location;
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
import java.time.LocalDate;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    private ObjectMapper objectMapper;

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String WEATHER_API_URL =
            "https://weather.googleapis.com/v1/forecast/days:lookup?key=%s&location.latitude=%f&location.longitude=%f";

    public WeatherRecommendationDTO getWeatherRecommendation(Location location, LocalDate date) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String url = String.format(Locale.US, WEATHER_API_URL, apiKey, latitude, longitude);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        WeatherRecommendationDTO weatherRecommendation = new WeatherRecommendationDTO();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());

            JsonNode forecastDays = root.get("forecastDays");
            if (forecastDays == null || !forecastDays.isArray()) {
                weatherRecommendation.setSuitable(false);
                weatherRecommendation.setMessage("No forecast data available for the selected date.");
                return weatherRecommendation;
            }

            for (JsonNode day : forecastDays) {
                JsonNode displayDate = day.get("displayDate");
                if (displayDate == null) continue;

                LocalDate forecastDate = LocalDate.of(
                        displayDate.get("year").asInt(),
                        displayDate.get("month").asInt(),
                        displayDate.get("day").asInt()
                );

                if (forecastDate.equals(date)) {
                    int thunderstormProbability = day.at("/daytimeForecast/thunderstormProbability").asInt();
                    int precipitation = day.at("/daytimeForecast/precipitation/probability/percent").asInt();
                    String description = day.at("/daytimeForecast/weatherCondition/description/text").asText();
                    String cloudCover = day.at("/daytimeForecast/cloudCover").asText();

                    boolean isSuitable = precipitation < 50;

                    String message = isSuitable
                            ? "The weather is favorable. Open locations like parks are suitable"
                            : "Weather is unfavorable. Prefer indoor locations (e.g., museums, galleries, restaurants)";

                    weatherRecommendation.setSuitable(isSuitable);
                    weatherRecommendation.setThunderstormProbability(thunderstormProbability);
                    weatherRecommendation.setPrecipitationProbability(precipitation);
                    weatherRecommendation.setDescription(description);
                    weatherRecommendation.setMessage(message);
                    weatherRecommendation.setCloudCover(cloudCover+"%");

                    return weatherRecommendation;
                }
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Weather API call failed.", e);
        }

        return weatherRecommendation;
    }
}
