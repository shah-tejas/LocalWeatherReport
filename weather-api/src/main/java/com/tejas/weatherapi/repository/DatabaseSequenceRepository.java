package com.tejas.weatherapi.repository;

import com.tejas.weatherapi.model.DatabaseSequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DatabaseSequenceRepository extends MongoRepository<DatabaseSequence, Long> {
}
