package com.tejas.weatherapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class WeeklyDataForCity {
    String dayOfWeek;
    String date;
    double minTemp;
    double maxTemp;
    String weatherCondition;
}
