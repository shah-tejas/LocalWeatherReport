package com.tejas.weatherapi.controller;

import com.tejas.weatherapi.model.City;
import com.tejas.weatherapi.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CityController {
    @Autowired
    CityRepository cityRepository;

    @GetMapping("/api/cities")
    public ResponseEntity<List<City>> getAllCities() {
        return new ResponseEntity<>(cityRepository.findAll(), HttpStatus.OK);
    }
}
