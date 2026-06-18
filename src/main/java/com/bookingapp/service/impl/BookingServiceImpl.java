package com.bookingapp.service.impl;

import com.bookingapp.exception.BookingConflictException;
import com.bookingapp.exception.ResourceNotFoundException;
import com.bookingapp.exception.UnauthorizedException;
import com.bookingapp.model.dto.BookingDto;
import com.bookingapp.model.entity.Booking;
import com.bookingapp.model.entity.Room;
import com.bookingapp.model.entity.User;
import com.bookingapp.model.enums.BookingStatus;
import com.bookingapp.model.enums.UserRole;
import com.bookingapp.repository.BookingRepository;
import com.bookingapp.service.BookingService;
import com.bookingapp.service.RoomService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;

    public BookingServiceImpl(BookingRepository bookingRepository, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
    }

    @Override
    public Booking createBooking(UUID roomId, BookingDto bookingDto, User guest) {
        Room room = roomService.findById(roomId);

        if (!bookingDto.getCheckOut().isAfter(bookingDto.getCheckIn())) {
            throw new BookingConflictException("Check-out date must be after check-in date");
        }

        boolean overlaps = bookingRepository.existsOverlappingBooking(
                roomId, bookingDto.getCheckIn(), bookingDto.getCheckOut(), BookingStatus.CANCELLED);
        if (overlaps) {
            throw new BookingConflictException("This room is already booked for the selected dates");
        }

        long nights = ChronoUnit.DAYS.between(bookingDto.getCheckIn(), bookingDto.getCheckOut());
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking();
        booking.setCheckIn(bookingDto.getCheckIn());
        booking.setCheckOut(bookingDto.getCheckOut());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setGuest(guest);
        booking.setRoom(room);

        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(UUID bookingId, User currentUser) {
        Booking booking = findById(bookingId);
        boolean isOwner = booking.getGuest().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to cancel this booking");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public Booking findById(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    @Override
    public List<Booking> findAllByGuest(UUID guestId) {
        return bookingRepository.findAllByGuestId(guestId);
    }

    @Override
    public List<Booking> findAllByRoom(UUID roomId) {
        return bookingRepository.findAllByRoomId(roomId);
    }
}
