package com.tejas.weatherapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

@AllArgsConstructor
public @Data class City {

    @Transient
    public static final String SEQUENCE_NAME = "city_sequence";

    @Id
    private long id;
    @Indexed(unique = true)
    private String city;
}
