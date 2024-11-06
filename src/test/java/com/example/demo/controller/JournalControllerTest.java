package com.example.demo.controller;

import com.example.demo.entity.Journal;
import com.example.demo.entity.User;
import com.example.demo.repo.JournalRepo;
import com.example.demo.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JournalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll(); // Clear the database before each test
        journalRepo.deleteAll(); // Clear the journals
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testCreateJournal() throws Exception {
        userRepo.save(new User(null, "testUser", "password", null, null));

        mockMvc.perform(post("/journal")
                        .contentType("application/json")
                        .content("{\"title\":\"Test Journal\", \"description\":\"Test Description\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testUpdateJournal() throws Exception {
        User user = userRepo.save(new User(null, "testUser", "password", null, null));
        Journal journal = new Journal(null, "Original Title", "Description", null);
        journalRepo.save(journal);

        mockMvc.perform(put("/journal/id/" + journal.getId())
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Title\", \"description\":\"Updated Description\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testDeleteJournal() throws Exception {
        User user = userRepo.save(new User(null, "testUser", "password", null, null));
        Journal journal = new Journal(null, "Test Title", "Test Description", null);
        journalRepo.save(journal);

        mockMvc.perform(delete("/journal/id/" + journal.getId()))
                .andExpect(status().isNoContent());
    }
}
