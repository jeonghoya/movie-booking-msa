// src/main/java/com/example/demo/dto/ScreeningRequestDto.java
package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ScreeningRequestDto {
    private Long movieId;
    private Long screeningHallId;
    private LocalDateTime screeningTime;
}