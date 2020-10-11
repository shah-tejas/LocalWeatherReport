package com.tejas.weatherapi.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class WeatherUtil {

    public final static Map<String, CityCoord> cities;

    static {
        cities = new HashMap<>();
        cities.put("Boston", new CityCoord("42.3601", "71.0589"));
        cities.put("Chicago", new CityCoord("41.8781", "87.6298"));
        cities.put("New York", new CityCoord("40.7128", "74.0060"));
        cities.put("Miami", new CityCoord("25.7617", "80.1918"));
        cities.put("San Francisco", new CityCoord("37.7749", "122.4194"));
        cities.put("Seattle", new CityCoord("47.6062", "122.3321"));
        cities.put("Baltimore", new CityCoord("39.2904", "76.6122"));
        cities.put("Atlanta", new CityCoord("33.7490", "84.3880"));
        cities.put("Los Angeles", new CityCoord("34.0522", "118.2437"));
        cities.put("Philadelphia", new CityCoord("39.9526", "75.1652"));
    }

    public final static String API_BASE_URL = "https://api.openweathermap.org/data/2.5/onecall/timemachine";

    @AllArgsConstructor
    public static @Data class CityCoord {
        private String latitude;
        private String longitude;
    }
}
