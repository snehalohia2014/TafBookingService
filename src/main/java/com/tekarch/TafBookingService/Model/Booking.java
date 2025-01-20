package com.tekarch.TafBookingService.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    private Long id;
    private User user;
    private Flight flight;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
