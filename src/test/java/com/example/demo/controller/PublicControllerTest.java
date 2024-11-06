package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll(); // Clear the database before each test
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User(null, "newUser", "password", null, null);

        mockMvc.perform(post("/public/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newUser\", \"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newUser"));
    }

    @Test
    void testCreateUserWithExistingUsername() throws Exception {
        userRepo.save(new User(null, "existingUser", "password", null, null));

        mockMvc.perform(post("/public/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"existingUser\", \"password\":\"newPassword\"}"))
                .andExpect(status().isBadRequest());
    }
}
