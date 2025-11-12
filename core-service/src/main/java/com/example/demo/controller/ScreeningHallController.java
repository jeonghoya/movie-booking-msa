// src/main/java/com/example/demo/controller/ScreeningHallController.java
package com.example.demo.controller;

import com.example.demo.domain.ScreeningHall;
import com.example.demo.repository.ScreeningHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/core/screening-halls")
@RequiredArgsConstructor
public class ScreeningHallController {

    private final ScreeningHallRepository screeningHallRepository;

    @GetMapping
    public ResponseEntity<List<ScreeningHall>> getAllScreeningHalls() {
        return ResponseEntity.ok(screeningHallRepository.findAll());
    }

}