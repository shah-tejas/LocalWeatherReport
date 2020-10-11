package com.tejas.weatherapi.runner;

import com.tejas.weatherapi.model.City;
import com.tejas.weatherapi.model.WeatherCondition;
import com.tejas.weatherapi.model.WeatherData;
import com.tejas.weatherapi.repository.CityRepository;
import com.tejas.weatherapi.repository.DatabaseSequenceRepository;
import com.tejas.weatherapi.repository.WeatherDataRepository;
import com.tejas.weatherapi.service.SequenceGeneratorService;
import com.tejas.weatherapi.service.WeatherDataService;
import com.tejas.weatherapi.util.DateUtil;
import com.tejas.weatherapi.util.WeatherUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherInitializerRunner implements CommandLineRunner {
    @Autowired
    WeatherDataRepository weatherDataRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    DatabaseSequenceRepository databaseSequenceRepository;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    WeatherDataService weatherDataService;
    @Value("${API_KEY}")
    private String API_KEY;

    /*
    @Override
    public void run(String... args) throws Exception {
        // clear database
        System.out.println("==========Deleting all entries==========");
        weatherDataRepository.deleteAll();
        cityRepository.deleteAll();
        databaseSequenceRepository.deleteAll();

        // save documents to mongodb
        cityRepository.saveAll(Arrays.asList(
                new City(sequenceGeneratorService.generateSequence(City.SEQUENCE_NAME), "Boston"),
                new City(sequenceGeneratorService.generateSequence(City.SEQUENCE_NAME), "New York"),
                new City(sequenceGeneratorService.generateSequence(City.SEQUENCE_NAME), "Chicago"),
                new City(sequenceGeneratorService.generateSequence(City.SEQUENCE_NAME), "Seattle"),
                new City(sequenceGeneratorService.generateSequence(City.SEQUENCE_NAME), "Los Angeles")
        ));
        weatherDataRepository.saveAll(Arrays.asList(
                new WeatherData(sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        cityRepository.findAllByCity("Boston").get(0),
                        Instant.now().toEpochMilli(),
                        11.1,
                        WeatherCondition.CLEAR_SKY),
                new WeatherData(sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        cityRepository.findAllByCity("Boston").get(0),
                        Instant.now().toEpochMilli()-100,
                        11.3,
                        WeatherCondition.CLEAR_SKY),
                new WeatherData(sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        cityRepository.findAllByCity("Boston").get(0),
                        Instant.now().toEpochMilli()-200,
                        11.5,
                        WeatherCondition.CLEAR_SKY),
                new WeatherData(sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        cityRepository.findAllByCity("Chicago").get(0),
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

        // fetch by city
        System.out.println("======Fetch All by City Boston=====");
        weatherDataList = weatherDataRepository.findAllByCity("Boston");
        weatherDataList.forEach(System.out::println);

        // fetch all cities
        System.out.println("======Fetch All Cities=====");
        List<City> cities = cityRepository.findAll();
        cities.forEach(System.out::println);

        // fetch current for Boston
//        System.out.println("======Fetch All for Boston And for Current Day=====");
//        long startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//        long endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//        System.out.println("Start Of Day: " + startOfDay);
//        System.out.println("End Of Day: " + endOfDay);
//        weatherDataList = weatherDataRepository.findAllByCityAndTimestampIsBetween("Boston", startOfDay, endOfDay);
//        weatherDataList.forEach(System.out::println);
//        weatherDataList = weatherDataService.getHourlyTempForToday("Boston");
//        weatherDataList.forEach(System.out::println);
//        weatherDataList = weatherDataRepository.findAllByTimestampGreaterThanEqual(startOfDay);
//        weatherDataList.forEach(System.out::println);
//        weatherDataList = weatherDataRepository.findAllByTemperatureIsBetween(11.1, 11.5);
//        weatherDataList.forEach(System.out::println);
    }
     */

    @Override
    public void run(String... args) throws Exception {
        clearDatabase();

        fetchDataForAllCities();

//        // fetch all documents
//        System.out.println("======Fetch All=====");
//        List<WeatherData> weatherDataList = weatherDataRepository.findAll();
//        weatherDataList.forEach(System.out::println);
    }

    public void clearDatabase() {
        // clear database
        System.out.println("==========Deleting all entries==========");
        weatherDataRepository.deleteAll();
        cityRepository.deleteAll();
        databaseSequenceRepository.deleteAll();

        // save cities to mongodb
        List<City> cities = new ArrayList<>();
        for(String city : WeatherUtil.cities.keySet()) {
            cities.add(new City(sequenceGeneratorService.generateSequence(City.SEQUENCE_NAME), city));
        }
        cityRepository.saveAll(cities);
    }

    public void fetchDataForAllCities() throws IOException, InterruptedException {
        System.out.println("Fetching weather data...");
        LocalDateTime now = LocalDateTime.now();

        // fetch for last 5 days
        for(int i = 1; i <= 5; i++) {
            LocalDateTime previous = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth()-i, now.getHour(), now.getMinute());
            String timestamp = String.valueOf(previous.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000);

            // fetch for all cities
            for(String city : WeatherUtil.cities.keySet()){
                fetchDataForCityForTime(city, timestamp);
            }
        }

        System.out.println("Fetching weather data completed!");
    }

    public void fetchDataForCityForTime(String city, String timestamp) throws IOException, InterruptedException {
//        System.out.println(String.format("=====Fetching data for all %s at %s=====", city, timestamp));
        WeatherUtil.CityCoord cityCoord = WeatherUtil.cities.get(city);
        if(cityCoord == null) return;

        var client = HttpClient.newHttpClient();

        URI uri = URI.create(WeatherUtil.API_BASE_URL
                    + "?appid=" + API_KEY
                    + "&lat=" + cityCoord.getLatitude()
                    + "&lon=" + cityCoord.getLongitude()
                    + "&units=metric"
                    + "&dt=" + timestamp);

        var request = HttpRequest.newBuilder(uri)
                .header("accept", "application/json")
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<WeatherData> weatherDataList = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(response.body());
            JSONArray hourly = json.getJSONArray("hourly");
            for(int i = 0; i < hourly.length(); i++) {
                JSONObject obj = hourly.getJSONObject(i);
                String weather = obj.getJSONArray("weather").getJSONObject(0).getString("main");
                WeatherData weatherData = new WeatherData(
                        sequenceGeneratorService.generateSequence(WeatherData.SEQUENCE_NAME),
                        cityRepository.findAllByCity(city).get(0),
                        obj.getLong("dt"),
                        obj.getDouble("temp"),
                        WeatherCondition.valueOf(weather)
                );
                weatherDataList.add(weatherData);
            }
        } catch (JSONException e) {
            System.out.println(String.format("Unable to fetch data for ${} at ${}", city, timestamp));
            return;
        }

        weatherDataRepository.saveAll(weatherDataList);
    }
}
