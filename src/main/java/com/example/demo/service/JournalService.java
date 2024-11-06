package com.example.demo.service;

import com.example.demo.entity.Journal;
import com.example.demo.entity.User;
import com.example.demo.repo.JournalRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalService {

    private final JournalRepo journalRepo;
    private final UserService userService;

    public JournalService(JournalRepo journalRepo, UserService userService) {
        this.journalRepo = journalRepo;
        this.userService = userService;
    }

    @Transactional
    public void saveJournal(Journal journal, String username) {
        try {
            User user = userService.getUserByUsername(username);
            journal.setDate(LocalDateTime.now());
            Journal saved = journalRepo.save(journal);
            user.getJournalList().add(saved);
            userService.saveUser(user);
        }catch (Exception e){
            throw new RuntimeException("Error saving journal");
        }
    }

    public void saveJournal(Journal journal) {
        journalRepo.save(journal);
    }

    public List<Journal> getAllJournals() {
        return journalRepo.findAll();
    }

    public Optional<Journal> getJournalById(ObjectId id) {
        return journalRepo.findById(id);
    }

    @Transactional
    public boolean deleteJournalById(ObjectId id,String username) {
        boolean removed;
        try {
            User user = userService.getUserByUsername(username);
            removed = user.getJournalList().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalRepo.deleteById(id);
            }
        }catch (Exception e){
            throw new RuntimeException("Error deleting journal");
        }
        return removed;
    }

    public List<Journal> getJournalsByUser(String username) {
        User user=userService.getUserByUsername(username);
        return user.getJournalList();
    }
}
