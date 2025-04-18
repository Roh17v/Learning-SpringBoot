package com.rohitverma.journal.service;

import com.rohitverma.journal.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${API_KEY}")
    private String API_KEY;
    private static final String BASE_URL="http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    RestTemplate restTemplate;

    public WeatherResponse getWeather(String city)
    {
        String finalUrl = BASE_URL.replace("API_KEY",API_KEY).replace("CITY",city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }

}
