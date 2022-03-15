package com.sweethome.Booking.repository;

import com.sweethome.Booking.entities.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingInfoEntity, Integer> {
}
