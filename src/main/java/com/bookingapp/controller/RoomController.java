package com.bookingapp.controller;

import com.bookingapp.config.UserPrincipal;
import com.bookingapp.model.dto.BookingDto;
import com.bookingapp.model.dto.RoomDto;
import com.bookingapp.model.entity.Room;
import com.bookingapp.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.findAllAvailable());
        return "rooms/list";
    }

    @GetMapping("/{id}")
    public String roomDetail(@PathVariable UUID id, Model model) {
        model.addAttribute("room", roomService.findById(id));
        model.addAttribute("bookingDto", new BookingDto());
        return "rooms/detail";
    }

    @GetMapping("/new")
    public String newRoomForm(Model model) {
        model.addAttribute("roomDto", new RoomDto());
        return "rooms/form";
    }

    @PostMapping("/new")
    public String createRoom(@Valid @ModelAttribute("roomDto") RoomDto roomDto,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal UserPrincipal principal,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "rooms/form";
        }
        Room room = roomService.createRoom(roomDto, principal.getUser());
        return "redirect:/rooms/" + room.getId();
    }

    @GetMapping("/{id}/edit")
    public String editRoomForm(@PathVariable UUID id, Model model) {
        Room room = roomService.findById(id);
        RoomDto dto = new RoomDto();
        dto.setTitle(room.getTitle());
        dto.setDescription(room.getDescription());
        dto.setLocation(room.getLocation());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setMaxGuests(room.getMaxGuests());
        dto.setImageUrl(room.getImageUrl());
        dto.setAvailable(room.isAvailable());

        model.addAttribute("roomDto", dto);
        model.addAttribute("roomId", id);
        model.addAttribute("isEdit", true);
        return "rooms/form";
    }

    @PostMapping("/{id}/edit")
    public String updateRoom(@PathVariable UUID id,
                              @Valid @ModelAttribute("roomDto") RoomDto roomDto,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal UserPrincipal principal,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("roomId", id);
            return "rooms/form";
        }
        roomService.updateRoom(id, roomDto, principal.getUser());
        return "redirect:/rooms/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteRoom(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        roomService.deleteRoom(id, principal.getUser());
        return "redirect:/rooms";
    }

    @GetMapping("/my")
    public String myRooms(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("rooms", roomService.findAllByHost(principal.getUser().getId()));
        return "rooms/my-rooms";
    }
}
