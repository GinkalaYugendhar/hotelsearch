package com.hg.hotelsearch.service;

import com.hg.hotelsearch.entity.Hotel;
import com.hg.hotelsearch.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel createHotel(Hotel hotel) {
        log.info("Creating hotel: {}", hotel.getHotelName());
        Hotel savedHotel = hotelRepository.save(hotel);
        log.debug("Hotel created successfully with ID: {}", savedHotel.getHotelId());
        return savedHotel;
    }

    public List<Hotel> getAllHotels() {
        log.info("Fetching all hotels...");
        List<Hotel> hotels = hotelRepository.findAll();
        log.debug("Total hotels found: {}", hotels.size());
        return hotels;
    }
}
