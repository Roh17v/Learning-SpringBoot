package com.rohitverma.journal.service;

import com.rohitverma.journal.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    private static final String API_KEY="82aad8e66afe13dd1118bab3197661f0";
    private static final String BASE_URL="http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    RestTemplate restTemplate;

    public WeatherResponse getWeather()
    {
        String finalUrl = BASE_URL.replace("API_KEY",API_KEY).replace("CITY","mumbai");
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }

}
