package com.hg.hotelsearch.repository;

import com.hg.hotelsearch.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByHotelCity(String hotelCity);
    List<Hotel> findByHotelCityAndHotelAddressContaining(String hotelCity, String pincode);
}
