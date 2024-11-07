package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config")
@Data
@NoArgsConstructor
public class Config {
    private String key;
    private String value;
}
