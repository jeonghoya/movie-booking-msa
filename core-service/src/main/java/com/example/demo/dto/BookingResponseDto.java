// src/main/java/com/example/demo/dto/BookingResponseDto.java
package com.example.demo.dto;

import com.example.demo.domain.Booking;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BookingResponseDto {
    private Long bookingId;
    private String movieTitle;
    private String screeningHallName;
    private LocalDateTime screeningTime;
    private List<String> seats;
    private LocalDateTime bookingTime;

    public BookingResponseDto(Booking booking) {
        this.bookingId = booking.getId();
        this.movieTitle = booking.getScreening().getMovie().getTitle();
        this.screeningHallName = booking.getScreening().getScreeningHall().getName();
        this.screeningTime = booking.getScreening().getScreeningTime();
        this.seats = booking.getSeats();
        this.bookingTime = booking.getBookingTime();
    }
}