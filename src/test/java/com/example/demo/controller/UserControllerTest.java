package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll(); // Clear the database before each test
    }

    @Test
    @WithMockUser(username = "testUser", password = "password")
    void testUpdateUser() throws Exception {
        userRepo.save(new User(null, "testUser", "password", null, null));

        mockMvc.perform(put("/user")
                        .contentType("application/json")
                        .content("{\"username\":\"updatedUser\", \"password\":\"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "password")
    void testDeleteUser() throws Exception {
        userRepo.save(new User(null, "testUser", "password", null, null));

        mockMvc.perform(delete("/user"))
                .andExpect(status().isNoContent());
    }
}
