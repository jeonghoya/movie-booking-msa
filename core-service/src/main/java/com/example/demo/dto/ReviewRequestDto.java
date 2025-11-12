// src/main/java/com/example/demo/dto/ReviewRequestDto.java
package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private String content;
    private int rating;
}