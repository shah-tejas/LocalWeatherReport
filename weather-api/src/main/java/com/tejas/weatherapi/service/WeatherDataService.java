package com.tejas.weatherapi.service;

import com.tejas.weatherapi.model.WeatherCondition;
import com.tejas.weatherapi.model.WeatherData;
import com.tejas.weatherapi.model.dto.CurrentWeatherForCity;
import com.tejas.weatherapi.model.dto.WeeklyDataForCity;
import com.tejas.weatherapi.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        long startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        long endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        return weatherDataRepository.findAllByCityAndTimestampBetweenOrderByTimestampAsc(city, startOfDay, endOfDay);
    }

    // get current weather details (min, max & condition) for city for day
    public List<WeeklyDataForCity> getCurrentWeekWeatherDetails(String city) {
        LocalDate localDate = LocalDate.now();

        List<WeeklyDataForCity> weeklyData = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            LocalDate dayToCalc = localDate.minusDays(i);
            String dayOfWeek = dayToCalc.getDayOfWeek().toString();

            if(i == 6) dayToCalc = localDate.minusDays(i-1);

            long startOfDay = dayToCalc.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
            long endOfDay = LocalDateTime.of(dayToCalc, LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
            List<WeatherData> currentDayData = weatherDataRepository.findByTimestampBetween(startOfDay, endOfDay);

            weeklyData.add(getWeatherDetailsForDay(currentDayData, city, (i == 6) ? dayOfWeek : localDate.minusDays(i).getDayOfWeek().toString()));
        }

        return weeklyData;

    }

    public WeeklyDataForCity getWeatherDetailsForDay(List<WeatherData> currentDayData, String city, String day) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        Map<String, Integer> weatherConditionIntegerMap = new HashMap<>();
        for(WeatherData weatherData : currentDayData) {
            if(weatherData.getCity().getCity().equals(city)) {
                if(weatherData.getTemperature() < min) {
                    min = weatherData.getTemperature();
                }
                if(weatherData.getTemperature() > max) {
                    max = weatherData.getTemperature();
                }
                int count = weatherConditionIntegerMap.containsKey(weatherData.getWeatherCondition().name())
                        ? weatherConditionIntegerMap.get(weatherData.getWeatherCondition().name())
                        : 0;
                weatherConditionIntegerMap.put(weatherData.getWeatherCondition().name(), count+1);
            }
        }
        int count = 0;
        String weatherCondition = new String();
        for(String condition : weatherConditionIntegerMap.keySet()) {
            if(weatherConditionIntegerMap.get(condition) > count) {
                count = weatherConditionIntegerMap.get(condition);
                weatherCondition = condition;
            }
        }

        return new WeeklyDataForCity(day, min, max, weatherCondition);
    }
}
