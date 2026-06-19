package com.bookingapp.controller;

import com.bookingapp.model.dto.RegisterDto;
import com.bookingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDto") RegisterDto registerDto,
                            BindingResult bindingResult,
                            Model model) {

        if (registerDto.getUsername() != null && userService.usernameExists(registerDto.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "This username is already taken");
        }
        if (registerDto.getEmail() != null && userService.emailExists(registerDto.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (registerDto.getPassword() != null && registerDto.getConfirmPassword() != null
                && !registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        userService.register(registerDto);
        return "redirect:/login?registered=true";
    }
}
