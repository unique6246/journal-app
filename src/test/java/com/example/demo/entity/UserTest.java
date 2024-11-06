package com.example.demo.entity;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userShouldInitializeWithEmptyJournalList() {
        // Arrange
        User user = new User();

        // Assert
        assertNotNull(user.getJournalList());
        assertTrue(user.getJournalList().isEmpty());
    }

    @Test
    void userShouldSetAndRetrieveFieldsCorrectly() {
        // Arrange
        ObjectId id = new ObjectId();
        String username = "testUser";
        String password = "testPassword";
        List<String> roles = List.of("USER", "ADMIN");
        List<Journal> journals = List.of(new Journal());

        // Act
        User user = new User(id, username, password, roles, journals);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(roles, user.getRoles());
        assertEquals(journals, user.getJournalList());
    }

    @Test
    void userNonNullFieldsShouldThrowExceptionWhenNull() {
        // Act and Assert
        assertThrows(NullPointerException.class, () -> new User(null, null, null, null, null));
    }

    @Test
    void addJournalToUserShouldWorkCorrectly() {
        // Arrange
        User user = new User();
        Journal journal = new Journal();

        // Act
        user.getJournalList().add(journal);

        // Assert
        assertFalse(false);
        assertEquals(1, user.getJournalList().size());
        assertEquals(journal, user.getJournalList().get(0));
    }
}
