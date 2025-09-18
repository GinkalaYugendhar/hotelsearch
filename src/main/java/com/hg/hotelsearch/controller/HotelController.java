package com.hg.hotelsearch.controller;

import com.hg.hotelsearch.entity.Hotel;
import com.hg.hotelsearch.service.HotelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class HotelController {

    private static final Logger log = LoggerFactory.getLogger(HotelController.class);

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/hotels/addhotel")
    public ResponseEntity<?> createHotel(@RequestBody Hotel hotel) {
        log.info("Request to create hotel: {}", hotel.getHotelName());
        try {
            Hotel saved = hotelService.createHotel(hotel);
            log.debug("Hotel created successfully with ID: {}", saved.getHotelId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Error creating hotel: {}", e.getMessage(), e);
            throw new RuntimeException("Internal Server Error");
        }
    }

    @GetMapping("/hotels/allhotels")
    public ResponseEntity<?> getAllHotels() {
        log.info("Fetching all hotels");
        List<Hotel> hotels = hotelService.getAllHotels();
        if (hotels.isEmpty()) {
            log.warn("No hotels found in database");
            throw new RuntimeException("No hotels found");
        }
        log.debug("Fetched {} hotels", hotels.size());
        return ResponseEntity.ok(hotels);

    }
}
