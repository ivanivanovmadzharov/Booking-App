package com.bookingapp.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("message", ex.getMessage());
        mav.setStatus(org.springframework.http.HttpStatus.NOT_FOUND);
        return mav;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorized(UnauthorizedException ex) {
        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("message", ex.getMessage());
        mav.setStatus(org.springframework.http.HttpStatus.FORBIDDEN);
        return mav;
    }

    @ExceptionHandler(BookingConflictException.class)
    public ModelAndView handleBookingConflict(BookingConflictException ex) {
        ModelAndView mav = new ModelAndView("error/conflict");
        mav.addObject("message", ex.getMessage());
        mav.setStatus(org.springframework.http.HttpStatus.CONFLICT);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneral(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("message", "Something went wrong. Please try again later.");
        mav.setStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}
