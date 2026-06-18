package com.bookingapp.service.impl;

import com.bookingapp.exception.ResourceNotFoundException;
import com.bookingapp.exception.UnauthorizedException;
import com.bookingapp.model.dto.RoomDto;
import com.bookingapp.model.entity.Room;
import com.bookingapp.model.entity.User;
import com.bookingapp.model.enums.UserRole;
import com.bookingapp.repository.RoomRepository;
import com.bookingapp.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(RoomDto roomDto, User host) {
        Room room = new Room();
        mapDtoToEntity(roomDto, room);
        room.setHost(host);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(UUID roomId, RoomDto roomDto, User currentUser) {
        Room room = findById(roomId);
        assertOwnerOrAdmin(room, currentUser);
        mapDtoToEntity(roomDto, room);
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(UUID roomId, User currentUser) {
        Room room = findById(roomId);
        assertOwnerOrAdmin(room, currentUser);
        roomRepository.delete(room);
    }

    @Override
    public Room findById(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    @Override
    public List<Room> findAllAvailable() {
        return roomRepository.findAllByAvailableTrue();
    }

    @Override
    public List<Room> findAllByHost(UUID hostId) {
        return roomRepository.findAllByHostId(hostId);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    private void assertOwnerOrAdmin(Room room, User currentUser) {
        boolean isOwner = room.getHost().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to modify this room");
        }
    }

    private void mapDtoToEntity(RoomDto dto, Room room) {
        room.setTitle(dto.getTitle());
        room.setDescription(dto.getDescription());
        room.setLocation(dto.getLocation());
        room.setPricePerNight(dto.getPricePerNight());
        room.setMaxGuests(dto.getMaxGuests());
        room.setImageUrl(dto.getImageUrl());
        room.setAvailable(dto.isAvailable());
    }
}
