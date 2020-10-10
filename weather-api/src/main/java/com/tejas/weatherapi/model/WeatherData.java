package com.tejas.weatherapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.UUID;

@AllArgsConstructor
public @Data class WeatherData {

    @Transient
    public static final String SEQUENCE_NAME = "weather_data_sequence";

    @Id
    private long id;
    private City city;
    private long timestamp;
    private double temperature;
    private WeatherCondition weatherCondition;
}
