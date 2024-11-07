package com.example.demo.repo;

import com.example.demo.entity.Config;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepo extends MongoRepository<Config, ObjectId> {
}
