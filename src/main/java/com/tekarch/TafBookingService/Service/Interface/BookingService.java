package com.tekarch.TafBookingService.Service.Interface;


import com.tekarch.TafBookingService.Model.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(Long userId, Long flightId);
    Booking getBookingById(Long bookingId);
    List<Booking> getBookingsByUserId(Long userId);
    Booking cancelBooking(Long bookingId);


}
