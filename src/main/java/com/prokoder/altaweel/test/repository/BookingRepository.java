package com.prokoder.altaweel.test.repository;

import com.prokoder.altaweel.test.entity.Booking;
import com.prokoder.altaweel.test.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomAndStatusAndCheckOutAfterAndCheckInBefore(Room room, Booking.BookingStatus status, LocalDate start, LocalDate end);
}
