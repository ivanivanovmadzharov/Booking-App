package com.bookingapp.service;

import com.bookingapp.model.dto.BookingDto;
import com.bookingapp.model.entity.Booking;
import com.bookingapp.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    Booking createBooking(UUID roomId, BookingDto bookingDto, User guest);

    void cancelBooking(UUID bookingId, User currentUser);

    Booking findById(UUID id);

    List<Booking> findAllByGuest(UUID guestId);

    List<Booking> findAllByRoom(UUID roomId);
}
