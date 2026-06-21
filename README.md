# StayEasy — Room Booking System

A Spring Boot 3.4 application for browsing, listing, and booking rooms, built for the Spring Fundamentals regular exam project.

## Tech Stack
- Java 17, Spring Boot 3.4.0
- Spring MVC + Thymeleaf (server-side rendering)
- Spring Data JPA + MySQL
- Spring Security (session-based auth, role-based access control)
- Bean Validation (Hibernate Validator)
- Lombok

## Domain Model
- **User** — `id (UUID)`, `username`, `email`, `password`, `role` (`GUEST`, `HOST`, `ADMIN`)
- **Room** — `id (UUID)`, `title`, `description`, `location`, `pricePerNight`, `maxGuests`, `available`, `imageUrl`, owned by a `User` (host)
- **Booking** — `id (UUID)`, `checkIn`, `checkOut`, `totalPrice`, `status` (`PENDING`, `CONFIRMED`, `CANCELLED`), linked to a `User` (guest) and a `Room`

## Roles & Permissions
| Role  | Can do                                                                 |
|-------|------------------------------------------------------------------------|
| GUEST | Browse rooms, create/cancel their own bookings                         |
| HOST  | Everything a GUEST can browse, plus create/edit/delete their own rooms |
| ADMIN | Everything, plus manage/cancel any booking or room                     |

## Setup

### 1. Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL 8+ running locally

### 2. Configure the database
By default the app connects to `jdbc:mysql://localhost:3306/booking_db` (auto-created) using `root`/`root`. Edit `src/main/resources/application.properties` if your credentials differ:

```properties
spring.datasource.username=root
spring.datasource.password=12345
```

### 3. Run the app
```bash
mvn spring-boot:run
```
The app starts on **http://localhost:8080**.

### 4. Demo accounts (auto-seeded on first run)
| Username  | Password  | Role  |
|-----------|-----------|-------|
| admin     | admin123  | ADMIN |
| hostuser  | host123   | HOST  |
| guestuser | guest123  | GUEST |

Three sample rooms are seeded automatically so the app is explorable right away.

## Project Structure
```
src/main/java/com/bookingapp/
├── config/        Security config, UserDetailsService, data seeding
├── controller/     MVC controllers (web pages, form handling)
├── exception/      Custom exceptions + global exception handler
├── model/
│   ├── entity/     JPA entities (User, Room, Booking)
│   ├── dto/        Validated form-backing objects
│   └── enums/      UserRole, BookingStatus
├── repository/     Spring Data JPA repositories
└── service/        Business logic layer + implementations
```

## Notes
- Passwords are hashed with BCrypt; never stored in plain text.
- Booking creation checks for overlapping date ranges on the same room before confirming.
- Only a room's host (or an admin) can edit/delete that room; only a booking's guest (or an admin) can cancel it.
