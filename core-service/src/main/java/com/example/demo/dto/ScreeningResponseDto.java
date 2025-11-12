// src/main/java/com/example/demo/dto/ScreeningResponseDto.java
package com.example.demo.dto;

import com.example.demo.domain.Screening;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScreeningResponseDto {
    private Long screeningId;
    private String screeningHallName;
    private LocalDateTime screeningTime;

    public ScreeningResponseDto(Screening screening) {
        this.screeningId = screening.getId();
        this.screeningHallName = screening.getScreeningHall().getName();
        this.screeningTime = screening.getScreeningTime();
    }
}