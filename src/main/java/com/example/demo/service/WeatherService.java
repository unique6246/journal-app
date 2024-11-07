package com.example.demo.service;

import com.example.demo.api.response.WeatherResponse;
import com.example.demo.cache.AppCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${Weather.Api.Key}")
    private String apiKey;


    private final RestTemplate restTemplate;
    private final AppCache appCache;

    public WeatherService(RestTemplate restTemplate, AppCache appCache) {
        this.restTemplate = restTemplate;
        this.appCache = appCache;
    }

    @Cacheable(value = "weather_of_city",key = "#city")
    public WeatherResponse getWeather(String city) {
        String url=appCache.APP_CACHE.get(AppCache.Keys.weather_api.toString())+"?query="+city+"&access_key="+apiKey;
        ResponseEntity<WeatherResponse> exchange = restTemplate.exchange(url, HttpMethod.GET, null, WeatherResponse.class);
        return exchange.getBody();
    }
}
