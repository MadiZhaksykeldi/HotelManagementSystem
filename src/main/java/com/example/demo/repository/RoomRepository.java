package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    public List<Room> findByType(String type);

    public Room findById(long id);
}