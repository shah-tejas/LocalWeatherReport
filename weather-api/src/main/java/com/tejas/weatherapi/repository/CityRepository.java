package com.tejas.weatherapi.repository;

import com.tejas.weatherapi.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityRepository extends MongoRepository<City, Long> {
    List<City>findAllByCity(String city);
}
