package com.tekarch.TafBookingService.Service;


import com.tekarch.TafBookingService.Model.Booking;
import com.tekarch.TafBookingService.Model.Flight;
import com.tekarch.TafBookingService.Model.User;
import com.tekarch.TafBookingService.Service.Interface.BookingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LogManager.getLogger(BookingServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${datastore.service.url}")
    private String DATASOURCE_SERVICE_URL;


    @Override
    public Booking createBooking(Long userId, Long flightId) {
        // Fetch User
        User user = restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/users/" + userId,
                HttpMethod.GET,
                null,
                User.class
        ).getBody();

        if (user == null) {
            throw new RuntimeException("User not found.");
        }

        // Check if flight exists and has available seats
        Flight flight = restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/flights/" + flightId,
                HttpMethod.GET,
                null,
                Flight.class
        ).getBody();

        if (flight == null) {
            throw new RuntimeException("Flight not found.");
        }

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats for this flight.");
        }

        // Reduce available seats by 1
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flight.setUpdatedAt(LocalDateTime.now());

        // Update flight available seats
        restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/flights/" + flightId,
                HttpMethod.PUT,
                new HttpEntity<>(flight),
                Flight.class
        );

        // Create a booking record
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus("Booked");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        // Save booking
        return restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/bookings",
                HttpMethod.POST,
                new HttpEntity<>(booking),
                Booking.class
        ).getBody();
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/bookings/" + bookingId,
                HttpMethod.GET,
                null,
                Booking.class
        ).getBody();
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        ResponseEntity<List<Booking>> response = restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/bookings/user/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Booking>>() {}
        );
        return response.getBody();
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        // Retrieve the booking
        Booking booking = restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/bookings/" + bookingId,
                HttpMethod.GET,
                null,
                Booking.class
        ).getBody();

        if (booking == null) {
            throw new RuntimeException("Booking not found.");
        }

        // Mark booking as Cancelled
        booking.setStatus("Cancelled");
        booking.setUpdatedAt(LocalDateTime.now());

        // Update booking
        return restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/bookings/" + bookingId,
                HttpMethod.PUT,
                new HttpEntity<>(booking),
                Booking.class
        ).getBody();
    }

    public List<Booking> getAllBookings() {
        ResponseEntity<List<Booking>> response = restTemplate.exchange(
                DATASOURCE_SERVICE_URL + "/bookings",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Booking>>() {}
        );
        return response.getBody();
    }

}
