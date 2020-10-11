package com.tejas.weatherapi.repository;

import com.tejas.weatherapi.model.City;
import com.tejas.weatherapi.model.WeatherData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WeatherDataRepository extends MongoRepository<WeatherData, Long> {
    @Query("{'city.city': ?0}")
    List<WeatherData> findAllByCity(String city);

    @Query("{'city.city': ?0}")
    List<WeatherData> findAllByCityAndTimestampIsBetween(String city, long startTime, long endTime);

    @Query("{'city.city': ?0}")
    List<WeatherData> findAllByCityAndTimestampBeforeOrderByTimestampDesc(String city, long time);

    @Query("{'city.city': ?0}")
    List<WeatherData> findAllByCityAndTimestampAfterOrderByTimestampAsc(String city, long time);
}
