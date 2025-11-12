// src/main/java/com/example/demo/dto/ScreeningDetailResponseDto.java
package com.example.demo.dto;

import com.example.demo.domain.Screening;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScreeningDetailResponseDto {

    private String movieTitle;
    private ScreeningHallDto screeningHall;
    private LocalDateTime screeningTime;

    // 상영관 정보를 담을 내부 DTO
    @Getter
    private static class ScreeningHallDto {
        private String name;
        private int totalRows;
        private int totalColumns;

        public ScreeningHallDto(String name, int totalRows, int totalColumns) {
            this.name = name;
            this.totalRows = totalRows;
            this.totalColumns = totalColumns;
        }
    }

    public ScreeningDetailResponseDto(Screening screening) {
        this.movieTitle = screening.getMovie().getTitle();
        this.screeningTime = screening.getScreeningTime();
        this.screeningHall = new ScreeningHallDto(
                screening.getScreeningHall().getName(),
                screening.getScreeningHall().getTotalRows(),
                screening.getScreeningHall().getTotalColumns()
        );
    }
}