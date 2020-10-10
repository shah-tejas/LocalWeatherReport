package com.tejas.weatherapi.repository;

import com.tejas.weatherapi.model.WeatherData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WeatherDataRepository extends MongoRepository<WeatherData, Long> {
    @Query("{'city.city': ?0}")
    List<WeatherData> findAllByCity(String city);
}
