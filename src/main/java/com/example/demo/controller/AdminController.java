package com.example.demo.controller;

import com.example.demo.cache.AppCache;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AppCache appCache;

    public AdminController(UserService userService, AppCache appCache) {
        this.userService = userService;
        this.appCache = appCache;
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getUsers() {
        List<User> allUsers = userService.getAllUsers();
        if(allUsers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveAdmin(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/refresh-cache")
    public ResponseEntity<?> refreshCache() {
        appCache.init();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
