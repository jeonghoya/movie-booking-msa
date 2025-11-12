package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieRequestDto {
    private String title;
    private String director;
    private String genre;
    private int runningTime;
}
