package com.example.demo.entity;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JournalTest {

    @Test
    void journalShouldInitializeCorrectly() {
        // Arrange
        ObjectId id = new ObjectId();
        String title = "Sample Journal";
        String description = "A test description";
        LocalDateTime date = LocalDateTime.now();

        // Act
        Journal journal = new Journal(id, title, description, date);

        // Assert
        assertEquals(id, journal.getId());
        assertEquals(title, journal.getTitle());
        assertEquals(description, journal.getDescription());
        assertEquals(date, journal.getDate());
    }

    @Test
    void journalNonNullFieldsShouldThrowExceptionWhenNull() {
        // Arrange, Act, and Assert
        assertThrows(NullPointerException.class, () -> new Journal(null, null, null, null));
    }

    @Test
    void journalShouldSetAndGetFieldsCorrectly() {
        // Arrange
        Journal journal = new Journal();

        // Act
        journal.setTitle("New Title");
        journal.setDescription("New Description");

        // Assert
        assertEquals("New Title", journal.getTitle());
        assertEquals("New Description", journal.getDescription());
    }
}
