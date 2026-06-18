package com.bookingapp.service;

import com.bookingapp.model.dto.RegisterDto;
import com.bookingapp.model.entity.User;

import java.util.UUID;

public interface UserService {

    User register(RegisterDto registerDto);

    User findByUsername(String username);

    User findById(UUID id);

    boolean usernameExists(String username);

    boolean emailExists(String email);
}
