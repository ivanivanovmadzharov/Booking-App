package com.bookingapp.service;

import com.bookingapp.model.dto.RoomDto;
import com.bookingapp.model.entity.Room;
import com.bookingapp.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    Room createRoom(RoomDto roomDto, User host);

    Room updateRoom(UUID roomId, RoomDto roomDto, User currentUser);

    void deleteRoom(UUID roomId, User currentUser);

    Room findById(UUID id);

    List<Room> findAllAvailable();

    List<Room> findAllByHost(UUID hostId);

    List<Room> findAll();
}
