package com.example.demo.service;

import com.example.demo.entity.Journal;
import com.example.demo.entity.User;
import com.example.demo.repo.JournalRepo;
import com.example.demo.repo.UserRepo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JournalServiceTest {

    @Mock
    private JournalRepo journalRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private JournalService journalService;

    private User mockUser;
    private Journal mockJournal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock data
        mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setJournalList(new ArrayList<>());

        mockJournal = new Journal();
        mockJournal.setId(new ObjectId("507f1f77bcf86cd799439011"));
        mockJournal.setDate(LocalDateTime.now());
    }

    @Test
    void testSaveJournal_Success() {
        when(userService.getUserByUsername("testUser")).thenReturn(mockUser);
        when(journalRepo.save(any(Journal.class))).thenReturn(mockJournal);

        journalService.saveJournal(mockJournal, "testUser");

        assertTrue(mockUser.getJournalList().contains(mockJournal));
        verify(userService, times(1)).saveUser(mockUser);
        verify(journalRepo, times(1)).save(mockJournal);
    }

    @Test
    void testGetAllJournals() {
        List<Journal> journals = List.of(mockJournal);
        when(journalRepo.findAll()).thenReturn(journals);

        List<Journal> result = journalService.getAllJournals();

        assertEquals(journals.size(), result.size());
        assertEquals(mockJournal, result.get(0));
    }

    @Test
    void testGetJournalById_Found() {
        when(journalRepo.findById(any(ObjectId.class))).thenReturn(Optional.of(mockJournal));

        Optional<Journal> result = journalService.getJournalById(mockJournal.getId());

        assertTrue(result.isPresent());
        assertEquals(mockJournal, result.get());
    }

    @Test
    void testDeleteJournalById_Success() {
        when(userService.getUserByUsername("testUser")).thenReturn(mockUser);
        mockUser.getJournalList().add(mockJournal);

        boolean result = journalService.deleteJournalById(mockJournal.getId(), "testUser");

        assertTrue(result);
        verify(journalRepo, times(1)).deleteById(mockJournal.getId());
    }

    @Test
    void testGetJournalsByUser() {
        when(userService.getUserByUsername("testUser")).thenReturn(mockUser);
        mockUser.getJournalList().add(mockJournal);

        List<Journal> result = journalService.getJournalsByUser("testUser");

        assertEquals(1, result.size());
        assertEquals(mockJournal, result.get(0));
    }
}
