package com.example.demo.service;

import com.example.demo.domain.Booking;
import com.example.demo.domain.Screening;
import com.example.demo.domain.User;
import com.example.demo.dto.BookingRequestDto;
import com.example.demo.dto.BookingResponseDto;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ScreeningRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//public class BookingService {
//
//    private final BookingRepository bookingRepository;
//    private final UserRepository userRepository;
//    private final ScreeningRepository screeningRepository; // MovieRepository 대신 주입
//    // private final MovieRepository movieRepository;
//
//    @Transactional
//    // ✨ 1. 파라미터를 Long userId -> String email 로 변경
//    public Booking createBooking(String email, BookingRequestDto requestDto) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        Screening screening = screeningRepository.findById(requestDto.getScreeningId())
//                .orElseThrow(() -> new IllegalArgumentException("상영 정보를 찾을 수 없습니다."));
//
//        // ✨ --- 좌석 중복 검사 로직 추가 ---
//        List<Booking> existingBookings = bookingRepository.findByScreeningId(screening.getId());
//        List<String> bookedSeats = existingBookings.stream()
//                .flatMap(booking -> booking.getSeats().stream())
//                .toList();
//
//        for (String requestedSeat : requestDto.getSeats()) {
//            if (bookedSeats.contains(requestedSeat)) {
//                throw new IllegalStateException("이미 예매된 좌석입니다: " + requestedSeat);
//            }
//        }
//        // --- 중복 검사 끝 ---
//
//        Booking booking = new Booking();
//        booking.setUser(user);
//        booking.setScreening(screening);
//        booking.setSeats(requestDto.getSeats());
//        booking.setBookingTime(LocalDateTime.now());
//
//        return bookingRepository.save(booking);
//    }
//
//    // --- ✨ '내 예매 내역 조회' 메소드 수정 ---
//    @Transactional(readOnly = true)
//    public List<BookingResponseDto> findMyBookings(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        List<Booking> myBookings = bookingRepository.findByUserId(user.getId());
//
//        // Booking 엔티티 리스트를 BookingResponseDto 리스트로 변환하여 반환
//        return myBookings.stream()
//                .map(BookingResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    // --- ✨ '예매 취소' 메소드 수정 (파라미터 변경) ---
//    @Transactional
//    public void cancelBooking(Long bookingId, String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new IllegalArgumentException("예매 내역을 찾을 수 없습니다."));
//
//        // (보안) 현재 로그인한 사용자가 예매한 내역이 맞는지 확인
//        if (!booking.getUser().getId().equals(user.getId())) {
//            throw new IllegalStateException("본인의 예매 내역만 취소할 수 있습니다.");
//        }
//
//        bookingRepository.delete(booking);
//    }
//}

public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository; // ✨ DB를 공유하므로 UserRepository 유지
    private final ScreeningRepository screeningRepository;

    @Transactional
    public Booking createBooking(BookingRequestDto requestDto) { // ✨ 파라미터에서 email 제거

        // ✨ SecurityContext에서 현재 세션의 사용자 이메일(ID)을 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Screening screening = screeningRepository.findById(requestDto.getScreeningId())
                .orElseThrow(() -> new IllegalArgumentException("상영 정보를 찾을 수 없습니다."));

        List<String> bookedSeats = bookingRepository.findByScreeningId(screening.getId()).stream()
                .flatMap(booking -> booking.getSeats().stream()).toList();

        for (String requestedSeat : requestDto.getSeats()) {
            if (bookedSeats.contains(requestedSeat)) {
                throw new IllegalStateException("이미 예매된 좌석입니다: " + requestedSeat);
            }
        }

        Booking booking = new Booking();
        booking.setUser(user); // ✨ 조회한 User 객체 사용
        booking.setScreening(screening);
        booking.setSeats(requestDto.getSeats());
        booking.setBookingTime(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> findMyBookings() { // ✨ 파라미터에서 email 제거
        // ✨ SecurityContext에서 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return bookingRepository.findByUserId(user.getId()).stream()
                .map(BookingResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(Long bookingId) { // ✨ 파라미터에서 email 제거
        // ✨ SecurityContext에서 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("예매 내역을 찾을 수 없습니다."));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("본인의 예매 내역만 취소할 수 있습니다.");
        }
        bookingRepository.delete(booking);
    }
}
