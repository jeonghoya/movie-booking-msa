// src/main/java/com/example/demo/controller/ScreeningController.java
package com.example.demo.controller;

import com.example.demo.domain.Screening;
import com.example.demo.dto.ScreeningAdminResponseDto;
import com.example.demo.dto.ScreeningDetailResponseDto;
import com.example.demo.dto.ScreeningRequestDto;
import com.example.demo.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core/screenings")
@RequiredArgsConstructor
public class ScreeningController {

    private final ScreeningService screeningService;

    // ✨ 반환 타입을 ResponseEntity<List<Screening>> -> ResponseEntity<List<ScreeningAdminResponseDto>> 로 변경
    @GetMapping
    public ResponseEntity<List<ScreeningAdminResponseDto>> getAllScreenings() {
        return ResponseEntity.ok(screeningService.getAllScreenings());
    }

    // ✨ 상영 정보 생성 API (ADMIN 전용)
    @PostMapping
    public ResponseEntity<Screening> createScreening(@RequestBody ScreeningRequestDto requestDto) {
        return ResponseEntity.ok(screeningService.createScreening(requestDto));
    }

    // ✨ 상영 정보 삭제 API (ADMIN 전용)
    @DeleteMapping("/{screeningId}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long screeningId) {
        screeningService.deleteScreening(screeningId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{screeningId}/booked-seats")
    public ResponseEntity<List<String>> getBookedSeats(@PathVariable Long screeningId) {
        return ResponseEntity.ok(screeningService.getBookedSeats(screeningId));
    }

    // ✨ 반환 타입을 ResponseEntity<Screening> -> ResponseEntity<ScreeningDetailResponseDto> 로 변경
    @GetMapping("/{screeningId}")
    public ResponseEntity<ScreeningDetailResponseDto> getScreeningDetails(@PathVariable Long screeningId) {
        ScreeningDetailResponseDto screeningDetails = screeningService.getScreeningDetails(screeningId);
        return ResponseEntity.ok(screeningDetails);
    }
}