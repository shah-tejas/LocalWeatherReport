package com.tejas.weatherapi.controller;

import com.tejas.weatherapi.model.dto.CurrentWeatherForCity;
import com.tejas.weatherapi.model.dto.WeeklyDataForCity;
import com.tejas.weatherapi.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WeatherDataController {

    @Autowired
    WeatherDataService weatherDataService;

    @GetMapping("/api/currentWeather")
    @ResponseBody
    public ResponseEntity<CurrentWeatherForCity> getCurrentWeather(@RequestParam String city) {
        return new ResponseEntity<>(weatherDataService.getCurrentWeather(city), HttpStatus.OK);
    }

    @GetMapping("/api/weekWeather")
    @ResponseBody
    public ResponseEntity<List<WeeklyDataForCity>> getWeeklyWeatherData(@RequestParam String city) {
        return new ResponseEntity<>(weatherDataService.getCurrentWeekWeatherDetails(city), HttpStatus.OK);
    }
}
