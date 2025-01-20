package com.tekarch.TafBookingService.Controller;


import com.tekarch.TafBookingService.Model.Booking;
import com.tekarch.TafBookingService.Service.BookingServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private static final Logger logger = LogManager.getLogger(BookingController.class);

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @GetMapping
    public ResponseEntity<?> getAllBookings(){
        return new ResponseEntity<>(bookingServiceImpl.getAllBookings(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestParam Long userId, @RequestParam Long flightId) {
        try {
            Booking booking = bookingServiceImpl.createBooking(userId, flightId);
            return new ResponseEntity<>(booking, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error occurred while creating booking: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingServiceImpl.getBookingById(bookingId);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error occurred while fetching booking: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable Long userId) {
        try {
            List<Booking> bookings = bookingServiceImpl.getBookingsByUserId(userId);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error occurred while fetching bookings for user: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingServiceImpl.cancelBooking(bookingId);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error occurred while cancelling booking: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @ExceptionHandler
    public ResponseEntity<String> respondWithError(Exception e) {
        logger.error("Exception Occurred. Details : {}", e.getMessage());
        return new ResponseEntity<>("Exception Occurred. More info :" + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
