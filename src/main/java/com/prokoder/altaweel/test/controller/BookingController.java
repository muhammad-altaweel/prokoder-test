package com.prokoder.altaweel.test.controller;

import com.prokoder.altaweel.test.entity.Booking;
import com.prokoder.altaweel.test.entity.Room;
import com.prokoder.altaweel.test.repository.BookingRepository;
import com.prokoder.altaweel.test.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        Room room = roomRepository.findById(booking.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.isAvailable()) throw new RuntimeException("Room not available");

        if (!bookingRepository
                .findByRoomAndStatusAndCheckOutAfterAndCheckInBefore(
                        room, Booking.BookingStatus.CONFIRMED, booking.getCheckIn(), booking.getCheckOut()).isEmpty()) throw new RuntimeException("Room already booked for selected dates");

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        Booking saved = bookingRepository.save(booking);

        System.out.println("Simulated email: Booking confirmed for " + booking.getCustomerName());

        return saved;
    }

    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if(booking.getStatus().equals(Booking.BookingStatus.CANCELLED))
            throw new RuntimeException("Booking Already Cancelled");

        booking.setStatus(Booking.BookingStatus.CANCELLED);

        Room room = booking.getRoom();
        room.setAvailable(true);
        bookingRepository.save(booking);
        return booking;
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @GetMapping()
    public ResponseEntity<Page<Booking>> getBookings(Pageable pageable) {
        return new ResponseEntity<>(bookingRepository.findAll(pageable), HttpStatus.OK);
    }

}
