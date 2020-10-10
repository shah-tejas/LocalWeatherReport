package com.tejas.weatherapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequence")
public @Data class DatabaseSequence {
    @Id
    private String id;
    private long seq;
}
