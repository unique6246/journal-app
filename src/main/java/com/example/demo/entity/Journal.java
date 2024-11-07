package com.example.demo.entity;

import com.example.demo.enums.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Journal {

    @Id
    private ObjectId id;
    @NonNull
    private String title;
    private String description;
    private LocalDateTime date;
    private Sentiment sentiment;

    public Journal(String s) {
    }

    public Journal(ObjectId objectId, String testJournal, String description, LocalDateTime now) {
    }
}
