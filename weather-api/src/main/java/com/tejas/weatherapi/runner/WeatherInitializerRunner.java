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

    @Override
    public void run(String... args) throws Exception {
        clearDatabase();
        fetchDataForAllCities();
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

        // fetch for all cities
        for(String city : WeatherUtil.cities.keySet()) {
            // fetch last 48 hours data
            fetchLatestDataForCity(city);

            // fetch for last 5 days
            for (int i = 1; i <= 5; i++) {
                LocalDateTime previous = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - i, now.getHour(), now.getMinute());
                String timestamp = String.valueOf(previous.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000);
                fetchDataForCityForTime(city, timestamp);
            }
        }

        System.out.println("Fetching weather data completed!");
    }

    public void fetchLatestDataForCity(String city) throws IOException, InterruptedException {
//        System.out.println(String.format("=====Fetching latest data for %s", city));
        WeatherUtil.CityCoord cityCoord = WeatherUtil.cities.get(city);
        if(cityCoord == null) return;

        var client = HttpClient.newHttpClient();

        // Query last 48 hours data
        URI uri1 = URI.create(WeatherUtil.API_BASE_URL
                + "?appid=" + API_KEY
                + "&lat=" + cityCoord.getLatitude()
                + "&lon=" + cityCoord.getLongitude()
                + "&units=metric");
        var request1 = HttpRequest.newBuilder(uri1)
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        saveDataFromResponse(response1, city);
    }

    public void fetchDataForCityForTime(String city, String timestamp) throws IOException, InterruptedException {
//        System.out.println(String.format("=====Fetching data for all %s at %s=====", city, timestamp));
        WeatherUtil.CityCoord cityCoord = WeatherUtil.cities.get(city);
        if(cityCoord == null) return;

        var client = HttpClient.newHttpClient();

        // Query historical records from last 5 days
        URI uri2 = URI.create(WeatherUtil.API_BASE_URL
                    + "/timemachine?appid=" + API_KEY
                    + "&lat=" + cityCoord.getLatitude()
                    + "&lon=" + cityCoord.getLongitude()
                    + "&units=metric"
                    + "&dt=" + timestamp);
        var request2 = HttpRequest.newBuilder(uri2)
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        saveDataFromResponse(response2, city);
    }

    public void saveDataFromResponse(HttpResponse<String> response, String city) {
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
            System.out.println(String.format("Unable to fetch data for %s", city));
            return;
        }

        weatherDataRepository.saveAll(weatherDataList);
    }
}
