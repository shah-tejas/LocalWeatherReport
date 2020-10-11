package com.tejas.weatherapi.service;

import com.tejas.weatherapi.model.WeatherData;
import com.tejas.weatherapi.model.dto.CurrentWeatherForCity;
import com.tejas.weatherapi.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class WeatherDataService {

    @Autowired
    WeatherDataRepository weatherDataRepository;

    // get current temp and condition
    public CurrentWeatherForCity getCurrentWeather(String city) {
        long currentTime = Instant.now().toEpochMilli() / 1000;
        WeatherData less = weatherDataRepository.findAllByCityAndTimestampBeforeOrderByTimestampDesc(city, currentTime).get(0);
        WeatherData greater = weatherDataRepository.findAllByCityAndTimestampAfterOrderByTimestampAsc(city, currentTime).get(0);

        WeatherData current = null;

        if(Math.abs(less.getTimestamp() - currentTime) > Math.abs(greater.getTimestamp() - currentTime)) {
            current = greater;
        } else {
            current = less;
        }

        return new CurrentWeatherForCity(city, current.getTemperature(), current.getWeatherCondition());
    }

    // get today's hourly temp for city
    public List<WeatherData> getHourlyTempForToday(String city) {
        long startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return weatherDataRepository.findAllByCityAndTimestampIsBetween(city, startOfDay, endOfDay);
    }

    // get current weather details for city
}
