// src/main/java/com/example/demo/dto/ScreeningAdminResponseDto.java
package com.example.demo.dto;

import com.example.demo.domain.Screening;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScreeningAdminResponseDto {
    private Long screeningId;
    private String movieTitle;
    private String screeningHallName;
    private LocalDateTime screeningTime;

    public ScreeningAdminResponseDto(Screening screening) {
        this.screeningId = screening.getId();
        this.movieTitle = screening.getMovie().getTitle();
        this.screeningHallName = screening.getScreeningHall().getName();
        this.screeningTime = screening.getScreeningTime();
    }
}