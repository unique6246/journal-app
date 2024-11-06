package com.example.demo.repo;

import com.example.demo.entity.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    void findByUsername_shouldReturnUserWhenUsernameExists() {
        // Arrange
        User user = new User(new ObjectId(), "testUser", "testPassword", null, null);
        userRepo.save(user);

        // Act
        User foundUser = userRepo.findByUsername("testUser");

        // Assert
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void findByUsername_shouldReturnNullWhenUsernameDoesNotExist() {
        // Act
        User foundUser = userRepo.findByUsername("nonExistentUser");

        // Assert
        assertNull(foundUser);
    }

    @Test
    void deleteByUsername_shouldDeleteUserWhenUsernameExists() {
        // Arrange
        User user = new User(new ObjectId(), "deleteUser", "testPassword", null, null);
        userRepo.save(user);

        // Act
        userRepo.deleteByUsername("deleteUser");

        // Assert
        assertNull(userRepo.findByUsername("deleteUser"));
    }

    @Test
    void uniqueUsernameConstraint_shouldThrowExceptionWhenDuplicateUsername() {
        // Arrange
        User user1 = new User(new ObjectId(), "uniqueUser", "password1", null, null);
        User user2 = new User(new ObjectId(), "uniqueUser", "password2", null, null);

        // Act
        userRepo.save(user1);
        assertThrows(DuplicateKeyException.class, () -> userRepo.save(user2));
    }
}
