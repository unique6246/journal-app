package com.example.demo.repo;

import com.example.demo.entity.Journal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalRepo extends MongoRepository<Journal, ObjectId> {
}
