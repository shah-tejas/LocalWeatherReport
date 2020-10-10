package com.tejas.weatherapi.runner;

import com.tejas.weatherapi.model.City;
import com.tejas.weatherapi.model.WeatherCondition;
import com.tejas.weatherapi.model.WeatherData;
import com.tejas.weatherapi.repository.WeatherDataRepository;
import com.tejas.weatherapi.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherInitializerRunner implements CommandLineRunner {
    @Autowired
    WeatherDataRepository weatherDataRepository;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Override
    public void run(String... args) throws Exception {
        // clear database
        System.out.println("==========Deleting all entries==========");
        weatherDataRepository.deleteAll();

        // save documents to mongodb
        weatherDataRepository.saveAll(Arrays.asList(
                new WeatherData(sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        new City("Boston"),
                        Instant.now().toEpochMilli(),
                        11.1,
                        WeatherCondition.CLEAR_SKY),
                new WeatherData(sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        new City("Boston"),
                        Instant.now().toEpochMilli()-100,
                        11.3,
                        WeatherCondition.CLEAR_SKY),
                new WeatherData(sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        new City("Boston"),
                        Instant.now().toEpochMilli()-200,
                        11.5,
                        WeatherCondition.CLEAR_SKY)
        ));

        // Initial List
        List<WeatherData> weatherDataList = null;

        // fetch all documents
        System.out.println("======Fetch All=====");
        weatherDataList = weatherDataRepository.findAll();
        weatherDataList.forEach(System.out::println);
    }
}
