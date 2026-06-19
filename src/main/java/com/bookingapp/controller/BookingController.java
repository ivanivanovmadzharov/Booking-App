package com.bookingapp.controller;

import com.bookingapp.config.UserPrincipal;
import com.bookingapp.model.dto.BookingDto;
import com.bookingapp.service.BookingService;
import com.bookingapp.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;

    public BookingController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @PostMapping("/rooms/{roomId}")
    public String createBooking(@PathVariable UUID roomId,
                                 @Valid @ModelAttribute("bookingDto") BookingDto bookingDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal UserPrincipal principal,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("room", roomService.findById(roomId));
            return "rooms/detail";
        }
        bookingService.createBooking(roomId, bookingDto, principal.getUser());
        return "redirect:/bookings/my";
    }

    @GetMapping("/my")
    public String myBookings(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("bookings", bookingService.findAllByGuest(principal.getUser().getId()));
        return "bookings/my-bookings";
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        bookingService.cancelBooking(id, principal.getUser());
        return "redirect:/bookings/my";
    }
}
