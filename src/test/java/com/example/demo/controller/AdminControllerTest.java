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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll(); // Clear the database before each test
    }

    @Test
    @WithMockUser(username = "adminUser", roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        userRepo.save(new User(null, "user1", "password", null, null));
        userRepo.save(new User(null, "user2", "password", null, null));

        mockMvc.perform(get("/admin/all-users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "adminUser", roles = "ADMIN")
    void testCreateAdmin() throws Exception {
        mockMvc.perform(post("/admin/create-admin")
                        .contentType("application/json")
                        .content("{\"username\":\"newAdmin\", \"password\":\"adminPassword\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "normalUser", roles = "USER")
    void testCreateAdminAsUserShouldFail() throws Exception {
        mockMvc.perform(post("/admin/create-admin")
                        .contentType("application/json")
                        .content("{\"username\":\"newAdmin\", \"password\":\"adminPassword\"}"))
                .andExpect(status().isForbidden());
    }
}
