package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
        mockUser.setId(new ObjectId("507f1f77bcf86cd799439011"));
    }

    @Test
    void testSaveNewUser() {
        userService.saveNewUser(mockUser);

        verify(userRepo, times(1)).save(mockUser);
        assertNotNull(mockUser.getPassword());
    }

    @Test
    void testSaveAdmin() {
        userService.saveAdmin(mockUser);

        verify(userRepo, times(1)).save(mockUser);
        assertTrue(mockUser.getRoles().contains("ADMIN"));
    }

    @Test
    void testGetAllUsers() {
        when(userRepo.findAll()).thenReturn(List.of(mockUser));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(mockUser, result.get(0));
    }

    @Test
    void testGetUserById() {
        when(userRepo.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.getUserById(mockUser.getId());

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(mockUser.getId());

        verify(userRepo, times(1)).deleteById(mockUser.getId());
    }

    @Test
    void testGetUserByUsername() {
        when(userRepo.findByUsername("testUser")).thenReturn(mockUser);

        User result = userService.getUserByUsername("testUser");

        assertEquals(mockUser, result);
    }
}
