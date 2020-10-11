package com.tejas.weatherapi.model.dto;

import com.tejas.weatherapi.model.WeatherCondition;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class CurrentWeatherForCity {
    private String city;
    private double temperature;
    private WeatherCondition weatherCondition;
}
