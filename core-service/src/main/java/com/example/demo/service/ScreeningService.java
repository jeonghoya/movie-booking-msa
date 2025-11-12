// src/main/java/com/example/demo/service/ScreeningService.java
package com.example.demo.service;

import com.example.demo.domain.Booking;
import com.example.demo.domain.Movie;
import com.example.demo.domain.Screening;
import com.example.demo.domain.ScreeningHall;
import com.example.demo.dto.ScreeningAdminResponseDto;
import com.example.demo.dto.ScreeningDetailResponseDto;
import com.example.demo.dto.ScreeningRequestDto;
import com.example.demo.dto.ScreeningResponseDto;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ScreeningHallRepository;
import com.example.demo.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository; // ✨ MovieRepository 주입 추가
    private final ScreeningHallRepository screeningHallRepository; // ✨ ScreeningHallRepository 주입 추가

    // --- ✨ 아래 상영 정보 생성 및 삭제 메소드를 추가합니다 ---

    @Transactional
    public Screening createScreening(ScreeningRequestDto requestDto) {
        Movie movie = movieRepository.findById(requestDto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다."));
        ScreeningHall hall = screeningHallRepository.findById(requestDto.getScreeningHallId())
                .orElseThrow(() -> new IllegalArgumentException("상영관을 찾을 수 없습니다."));

        // --- ✨ 시간 중복 검사 로직 ---
        LocalDateTime newStartTime = requestDto.getScreeningTime();
        LocalDateTime newEndTime = newStartTime.plusMinutes(movie.getRunningTime());

        List<Screening> existingScreenings = screeningRepository.findByScreeningHall(hall);
        for (Screening existing : existingScreenings) {
            LocalDateTime existingStartTime = existing.getScreeningTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(existing.getMovie().getRunningTime());

            // 겹치는 조건: (새 상영 시작 < 기존 상영 종료) AND (새 상영 종료 > 기존 상영 시작)
            if (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) {
                throw new IllegalStateException("해당 상영관의 해당 시간에는 이미 다른 영화가 상영 중입니다.");
            }
        }
        // --- 중복 검사 끝 ---

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setScreeningHall(hall);
        screening.setScreeningTime(requestDto.getScreeningTime());

        return screeningRepository.save(screening);
    }

    @Transactional
    public void deleteScreening(Long screeningId) {
        screeningRepository.deleteById(screeningId);
    }
    // 특정 영화의 모든 상영 시간표 조회
    @Transactional(readOnly = true)
    public List<ScreeningResponseDto> getScreeningsForMovie(Long movieId) {
        return screeningRepository.findByMovieId(movieId).stream()
                .map(ScreeningResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 상영의 예매된 좌석 목록 조회
    @Transactional(readOnly = true)
    public List<String> getBookedSeats(Long screeningId) {
        List<Booking> existingBookings = bookingRepository.findByScreeningId(screeningId);
        return existingBookings.stream()
                .flatMap(booking -> booking.getSeats().stream())
                .collect(Collectors.toList());
    }

    // ✨ 반환 타입을 Screening -> ScreeningDetailResponseDto 로 변경
    @Transactional(readOnly = true)
    public ScreeningDetailResponseDto getScreeningDetails(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("상영 정보를 찾을 수 없습니다."));
        return new ScreeningDetailResponseDto(screening);
    }

    // ✨ 반환 타입을 List<Screening> -> List<ScreeningAdminResponseDto> 로 변경
    @Transactional(readOnly = true)
    public List<ScreeningAdminResponseDto> getAllScreenings() {
        return screeningRepository.findAll().stream()
                .map(ScreeningAdminResponseDto::new)
                .collect(Collectors.toList());
    }
}