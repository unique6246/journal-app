package com.example.demo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class WeatherResponse {
    @JsonProperty("request")
    private Request request;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("current")
    private Current current;

    @Setter
    @Getter
    public static class Current {

        @JsonProperty("observation_time")
        private String observationTime;

        @JsonProperty("temperature")
        private Integer temperature;

        @JsonProperty("weather_icons")
        private List<String> weatherIcons = new ArrayList<>();

        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions = new ArrayList<>();

        @JsonProperty("feelslike")
        private Integer feelslike;

        @JsonProperty("wind_speed")
        private Integer windSpeed;

        @JsonProperty("wind_degree")
        private Integer windDegree;

        @JsonProperty("pressure")
        private Integer pressure;
    }

    @Setter
    @Getter
    public static class Location {
        @JsonProperty("name")
        private String name;

        @JsonProperty("country")
        private String country;

        @JsonProperty("region")
        private String region;

        @JsonProperty("lat")
        private String lat;

        @JsonProperty("lon")
        private String lon;

        @JsonProperty("timezone_id")
        private String timezoneId;

        @JsonProperty("localtime")
        private String localtime;

    }

    @Setter
    @Getter
    public static class Request {
        @JsonProperty("type")
        private String type;

        @JsonProperty("query")
        private String query;

        @JsonProperty("language")
        private String language;

    }
}
