package com.tejas.weatherapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class HourlyWeather {
    private int hour;
    private int temp;
}
