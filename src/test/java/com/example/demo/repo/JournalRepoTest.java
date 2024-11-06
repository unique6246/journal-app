package com.example.demo.repo;

import com.example.demo.entity.Journal;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class JournalRepoTest {

    @Autowired
    private JournalRepo journalRepo;

    @Test
    void saveAndFindById_shouldReturnJournalWhenIdExists() {
        // Arrange
        Journal journal = new Journal(new ObjectId(), "Test Journal", "Description", LocalDateTime.now());
        journalRepo.save(journal);

        // Act
        Optional<Journal> foundJournal = journalRepo.findById(journal.getId());

        // Assert
        assertTrue(foundJournal.isPresent());
        assertEquals(journal.getId(), foundJournal.get().getId());
        assertEquals("Test Journal", foundJournal.get().getTitle());
    }

    @Test
    void deleteById_shouldDeleteJournalWhenIdExists() {
        // Arrange
        Journal journal = new Journal(new ObjectId(), "Journal to Delete", "Description", LocalDateTime.now());
        journalRepo.save(journal);

        // Act
        journalRepo.deleteById(journal.getId());

        // Assert
        assertFalse(journalRepo.findById(journal.getId()).isPresent());
    }
}
