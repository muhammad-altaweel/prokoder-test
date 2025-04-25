package com.prokoder.altaweel.test.controller;

import com.prokoder.altaweel.test.entity.Room;
import com.prokoder.altaweel.test.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getRooms(@RequestParam(required = false) Boolean available) {
        return available != null ? roomRepository.findByAvailable(available) : roomRepository.findAll();
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }


}

