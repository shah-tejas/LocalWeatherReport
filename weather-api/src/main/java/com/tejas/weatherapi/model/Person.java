package com.tejas.weatherapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

public @Data class Person {
    @Id
    private String id;

    private String firstName;
    private String lastName;
}
