package com.example.demo.service;

import com.example.demo.entity.Journal;
import com.example.demo.entity.User;
import com.example.demo.repo.JournalRepo;
import com.example.demo.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private final UserRepo userRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public void saveUser(User user) {
        userRepo.save(user);
    }
    public void saveUpdateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ADMIN","USER"));
        userRepo.save(user);
    }
    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void deleteUserById(ObjectId id) {
        userRepo.deleteById(id);
    }

    public void deleteUserByUsername(String username) {
        userRepo.deleteByUsername(username);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

}
