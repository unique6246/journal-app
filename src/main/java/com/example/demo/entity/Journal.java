package com.example.demo.entity;

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

    public Journal(String s) {
    }
}
