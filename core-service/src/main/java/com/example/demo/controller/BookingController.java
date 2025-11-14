package com.example.demo.controller;

import com.example.demo.domain.Booking;
import com.example.demo.dto.BookingRequestDto;
import com.example.demo.dto.BookingResponseDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core/bookings")
@RequiredArgsConstructor
public class BookingController {

//    private final BookingService bookingService;
//
//    // ✨ 예매하기 API 수정
//    @PostMapping
//    public ResponseEntity<Booking> createBooking(@AuthenticationPrincipal UserDetails userDetails,
//                                                 @RequestBody BookingRequestDto requestDto) {
//        // @AuthenticationPrincipal 어노테이션으로 현재 인증된 사용자의 정보를 받아옴
//        // userDetails.getUsername()은 CustomUserDetailsService에서 설정한대로 사용자의 이메일을 반환함
//        Booking booking = bookingService.createBooking(userDetails.getUsername(), requestDto);
//        return ResponseEntity.ok(booking);
//    }
//
//    // ✨ 내 예매 목록 조회 API 수정
//    @GetMapping
//    public ResponseEntity<List<BookingResponseDto>> getUserBookings(@AuthenticationPrincipal UserDetails userDetails) {
//        // 서비스 메소드 호출 부분을 수정된 이름으로 변경
//        List<BookingResponseDto> bookings = bookingService.findMyBookings(userDetails.getUsername());
//        return ResponseEntity.ok(bookings);
//    }
//
//    // --- ✨ 아래 예매 취소 API를 새로 추가합니다. ---
//    @DeleteMapping("/{bookingId}")
//    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId,
//                                                @AuthenticationPrincipal UserDetails userDetails) {
//        // JWT에서 사용자의 이메일을 가져와 서비스 로직에 전달
//        bookingService.cancelBooking(bookingId, userDetails.getUsername());
//        return ResponseEntity.ok("예매가 성공적으로 취소되었습니다.");
//    }
    //private final BookingService bookingService;
    //private final UserRepository userRepository;

//    @PostMapping
//    public ResponseEntity<Booking> createBooking(@AuthenticationPrincipal UserDetails userDetails, @RequestBody BookingRequestDto requestDto) {
//        Booking booking = bookingService.createBooking(userDetails.getUsername(), requestDto);
//        return ResponseEntity.ok(booking);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<BookingResponseDto>> getUserBookings(@AuthenticationPrincipal UserDetails userDetails) {
//        List<BookingResponseDto> bookings = bookingService.findMyBookings(userDetails.getUsername());
//        return ResponseEntity.ok(bookings);
//    }
//
//    @DeleteMapping("/{bookingId}")
//    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId, @AuthenticationPrincipal UserDetails userDetails) {
//        bookingService.cancelBooking(bookingId, userDetails.getUsername());
//        return ResponseEntity.ok("예매가 성공적으로 취소되었습니다.");
//    }
    private final BookingService bookingService;
    // ✨ UserRepository 주입 삭제 (Controller는 Service만 알면 됨)

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDto requestDto) { // ✨ 파라미터 수정
        Booking booking = bookingService.createBooking(requestDto); // ✨ 파라미터 수정
        return ResponseEntity.ok(booking);
    }

//    @GetMapping
//    public ResponseEntity<List<BookingResponseDto>> getUserBookings() { // ✨ 파라미터 수정
//        List<BookingResponseDto> bookings = bookingService.findMyBookings(); // ✨ 파라미터 수정
//        return ResponseEntity.ok(bookings);
//    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getUserBookings(
            // ✨ @AuthenticationPrincipal을 사용해 세션 객체를 바로 주입받습니다.
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // ✨✨✨ 세션 확인 로직 ✨✨✨
        if (userDetails != null) {
            System.out.println("--- CORE-SERVICE 세션 확인 ---");
            System.out.println("Principal 객체 타입: " + userDetails.getClass().getName());
            System.out.println("사용자 이름(이메일): " + userDetails.getUsername());
            System.out.println("사용자 권한: " + userDetails.getAuthorities());
            System.out.println("-----------------------------");
        } else {
            System.out.println("--- CORE-SERVICE 세션 확인 ---");
            System.out.println("Principal 객체가 null입니다. (Anonymous 사용자)");
            System.out.println("-----------------------------");
        }
        // ✨✨✨✨✨✨✨✨✨✨✨✨

        List<BookingResponseDto> bookings = bookingService.findMyBookings();
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) { // ✨ 파라미터 수정
        bookingService.cancelBooking(bookingId); // ✨ 파라미터 수정
        return ResponseEntity.ok("예매가 성공적으로 취소되었습니다.");
    }
}
