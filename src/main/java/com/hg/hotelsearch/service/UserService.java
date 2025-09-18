package com.hg.hotelsearch.service;

import com.hg.hotelsearch.entity.Hotel;
import com.hg.hotelsearch.entity.User;
import com.hg.hotelsearch.repository.HotelRepository;
import com.hg.hotelsearch.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    public UserService(UserRepository userRepository, HotelRepository hotelRepository) {
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
    }

    public User createUser(User user) {
        log.info("Creating user: {}", user.getUserName());
        User savedUser = userRepository.save(user);
        log.debug("User created successfully with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users...");
        List<User> users = userRepository.findAll();
        log.debug("Total users found: {}", users.size());
        return users;
    }

    public Optional<User> login(String userName, String userPassword) {
        log.info("Attempting login for username: {}", userName);
        Optional<User> user = userRepository.findByUserNameAndUserPassword(userName, userPassword);
        if (user.isPresent()) {
            log.debug("Login successful for user: {}", userName);
        } else {
            log.warn("Login failed for username: {}", userName);
        }
        return user;
    }

    public List<Hotel> getHotelsByUser(User user) {
        log.info("Fetching hotels for user: {} in city: {}", user.getUserName(), user.getCity());
        List<Hotel> hotels = hotelRepository.findByHotelCity(user.getCity());
        log.debug("Found {} hotels for city: {}", hotels.size(), user.getCity());
        return hotels;
    }

    public List<Hotel> getHotelsByUserId(Long userId) {
        log.info("Fetching hotels for user ID: {}", userId);
        return userRepository.findById(userId)
                .map(user -> {
                    log.debug("User found: {}. Fetching hotels in city: {}", user.getName(), user.getCity());
                    List<Hotel> hotels = hotelRepository.findByHotelCity(user.getCity());
                    log.debug("Total hotels found in {}: {}", user.getCity(), hotels.size());
                    return hotels;
                })
                .orElseGet(() -> {
                    log.warn("No user found with ID: {}", userId);
                    return List.of();
                });
    }
}
