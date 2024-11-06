package com.example.demo.controller;

import com.example.demo.entity.Journal;
import com.example.demo.entity.User;
import com.example.demo.service.JournalService;
import com.example.demo.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {

    private final JournalService journalService;
    private final UserService userService;
    public JournalController(JournalService journalService, UserService userService) {
        this.journalService = journalService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getJournalsByUser() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User user = userService.getUserByUsername(username);
            List<Journal> allJournals = user.getJournalList();
            if (allJournals.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allJournals, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable("id") ObjectId id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUserByUsername(username);
        List<Journal> collect = user.getJournalList().stream().filter(x -> x.getId().equals(id)).toList();

        if (!collect.isEmpty()) {
            Optional<Journal> journal=journalService.getJournalById(id);
            if(journal.isPresent()) {
                return new ResponseEntity<>(journal.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Journal> addJournal(@RequestBody Journal journal) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalService.saveJournal(journal,username);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("id/{id}")
    public ResponseEntity<Journal> updateJournal(
            @PathVariable ObjectId id,
            @RequestBody Journal journal
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUserByUsername(username);
        List<Journal> collect = user.getJournalList().stream().filter(x -> x.getId().equals(id)).toList();

        if (!collect.isEmpty()) {
            Optional<Journal> journalEntity=journalService.getJournalById(id);
            if(journalEntity.isPresent()) {
                Journal old = journalEntity.get();
                old.setTitle(!journal.getTitle().isEmpty() ? journal.getTitle() : old.getTitle());
                old.setDescription(journal.getDescription() != null && !    journal.getDescription().isEmpty() ? journal.getDescription() : old.getDescription());
                journalService.saveJournal(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Journal> deleteJournal(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean removed = journalService.deleteJournalById(id, username);
        if (removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
