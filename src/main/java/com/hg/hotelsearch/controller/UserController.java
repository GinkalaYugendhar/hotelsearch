package com.hg.hotelsearch.controller;

import com.hg.hotelsearch.entity.User;
import com.hg.hotelsearch.entity.Hotel;
import com.hg.hotelsearch.service.UserService;
import com.hg.hotelsearch.service.HotelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final HotelService hotelService;

    public UserController(UserService userService, HotelService hotelService) {
        this.userService = userService;
        this.hotelService = hotelService;
    }

    @PostMapping("/users/adduser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        log.info("Request to create user: {}", user.getUserName());
        try {
            User saved = userService.createUser(user);
            log.debug("User created successfully with ID: {}", saved.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new RuntimeException("Internal Server Error");
        }
    }

    @GetMapping("/users/allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            log.warn("No users found in database");
            throw new RuntimeException("No users found");
        }
        log.debug("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/userhotels/{userId}")
    public ResponseEntity<List<Hotel>> getHotelsByUserId(@PathVariable Long userId) {
        log.info("Fetching hotels for user ID: {}", userId);
        List<Hotel> hotels = userService.getHotelsByUserId(userId);
        if (hotels.isEmpty()) {
            log.warn("No hotels found for user ID: {}", userId);
            throw new RuntimeException("No hotels found for the given user ID");
        }
        log.debug("Found {} hotels for user ID: {}", hotels.size(), userId);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping("/users/login")
    public ResponseEntity<List<Hotel>> loginUser(@RequestBody Map<String, String> credentials) {
        String userName = credentials.get("userName");
        String userPassword = credentials.get("userPassword");

        log.info("Login attempt for user: {}", userName);
        Optional<User> loginUser = userService.login(userName, userPassword);

        if (loginUser.isPresent()) {
            List<Hotel> hotels = userService.getHotelsByUser(loginUser.get());
            log.debug("User {} logged in successfully, fetched {} hotels",
                    userName, hotels.size());
            return ResponseEntity.ok(hotels);
        } else {
            log.warn("Login failed for user: {}", userName);
            throw new RuntimeException("Invalid username or password");
        }
    }
}
