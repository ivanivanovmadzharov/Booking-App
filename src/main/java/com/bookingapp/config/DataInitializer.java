package com.bookingapp.config;

import com.bookingapp.model.entity.Room;
import com.bookingapp.model.entity.User;
import com.bookingapp.model.enums.UserRole;
import com.bookingapp.repository.RoomRepository;
import com.bookingapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Seeds the database with a demo admin, host, guest and a couple of rooms
 * so the app is immediately explorable after first startup.
 * Safe to run multiple times: it checks for existing data first.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoomRepository roomRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // already seeded
        }

        User admin = createUser("admin", "admin@stayeasy.com", "admin123", UserRole.ADMIN);
        User host = createUser("hostuser", "host@stayeasy.com", "host123", UserRole.HOST);
        createUser("guestuser", "guest@stayeasy.com", "guest123", UserRole.GUEST);

        Room room1 = new Room();
        room1.setTitle("Cozy Mountain Cabin");
        room1.setDescription("A charming wooden cabin nestled in the mountains, perfect for a quiet getaway with stunning views.");
        room1.setLocation("Bansko, Bulgaria");
        room1.setPricePerNight(new BigDecimal("65.00"));
        room1.setMaxGuests(4);
        room1.setAvailable(true);
        room1.setHost(host);
        roomRepository.save(room1);

        Room room2 = new Room();
        room2.setTitle("Modern City Apartment");
        room2.setDescription("Stylish 2-bedroom apartment in the heart of the city, walking distance to all major attractions.");
        room2.setLocation("Sofia, Bulgaria");
        room2.setPricePerNight(new BigDecimal("90.00"));
        room2.setMaxGuests(3);
        room2.setAvailable(true);
        room2.setHost(host);
        roomRepository.save(room2);

        Room room3 = new Room();
        room3.setTitle("Seaside Studio");
        room3.setDescription("Compact studio just steps from the beach, ideal for couples or solo travelers.");
        room3.setLocation("Varna, Bulgaria");
        room3.setPricePerNight(new BigDecimal("50.00"));
        room3.setMaxGuests(2);
        room3.setAvailable(true);
        room3.setHost(admin);
        roomRepository.save(room3);
    }

    private User createUser(String username, String email, String rawPassword, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return userRepository.save(user);
    }
}
