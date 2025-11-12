// src/main/java/com/example/demo/controller/ReviewController.java
package com.example.demo.controller;

import com.example.demo.domain.Review;
import com.example.demo.dto.ReviewRequestDto;
import com.example.demo.dto.ReviewResponseDto;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

//    private final ReviewService reviewService;
//    private final UserRepository userRepository; // ✨ UserRepository 주입
//
//    // 특정 영화에 리뷰 작성
//    @PostMapping("/movies/{movieId}/reviews")
//    public ResponseEntity<String> createReview(@PathVariable Long movieId,
//                                               @RequestBody ReviewRequestDto requestDto,
//                                               @AuthenticationPrincipal UserDetails userDetails) {
//        reviewService.createReview(userDetails.getUsername(), movieId, requestDto);
//        return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
//    }
//
//    // 특정 영화의 모든 리뷰 조회
//    @GetMapping("/movies/{movieId}/reviews")
//    public ResponseEntity<List<ReviewResponseDto>> getReviewsForMovie(@PathVariable Long movieId) {
//        List<ReviewResponseDto> reviews = reviewService.getReviewsForMovie(movieId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    // --- ✨ 아래 수정 및 삭제 API를 새로 추가합니다. ---
//
//    // ✨ 리뷰 수정 API 수정
//    @PutMapping("/reviews/{reviewId}")
//    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
//                                               @RequestBody ReviewRequestDto requestDto,
//                                               @AuthenticationPrincipal UserDetails userDetails) {
//        // userDetails에서 이메일을 가져와 User 객체를 조회
//        User user = userRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
//        // 조회한 User 객체를 서비스로 전달
//        reviewService.updateReview(reviewId, user, requestDto);
//        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
//    }
//
//    // ✨ 리뷰 삭제 API 수정
//    @DeleteMapping("/reviews/{reviewId}")
//    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId,
//                                               @AuthenticationPrincipal UserDetails userDetails) {
//        User user = userRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
//        reviewService.deleteReview(reviewId, user);
//        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
//    }
//
//    // ✨ 관리자용 전체 리뷰 조회 API 추가
//    @GetMapping("/admin/reviews")
//    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
//        List<ReviewResponseDto> reviews = reviewService.getAllReviews();
//        return ResponseEntity.ok(reviews);
//    }
//
//    // ✨ 내가 쓴 리뷰 조회 API 추가
//    @GetMapping("/users/me/reviews")
//    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(@AuthenticationPrincipal UserDetails userDetails) {
//        List<ReviewResponseDto> myReviews = reviewService.getMyReviews(userDetails.getUsername());
//        return ResponseEntity.ok(myReviews);
//    }
private final ReviewService reviewService;
    // private final UserRepository userRepository; // ✨ 컨트롤러는 리포지토리를 직접 알 필요 없으므로 삭제

    @PostMapping("/api/core/movies/{movieId}/reviews")
    public ResponseEntity<Review> createReview(@PathVariable Long movieId, @RequestBody ReviewRequestDto requestDto) { // ✨ 파라미터 수정
        Review review = reviewService.createReview(movieId, requestDto); // ✨ 파라미터 수정
        // ✨ 모놀리식 버전의 "ok(String)" 대신 생성된 객체를 반환하도록 수정
        return ResponseEntity.ok(review);
    }

    @GetMapping("/api/core/movies/{movieId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsForMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(reviewService.getReviewsForMovie(movieId));
    }

    @PutMapping("/api/core/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDto requestDto) { // ✨ 파라미터 수정
        Review review = reviewService.updateReview(reviewId, requestDto); // ✨ 파라미터 수정
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/api/core/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) { // ✨ 파라미터 수정
        reviewService.deleteReview(reviewId); // ✨ 파라미터 수정
        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    }

    // --- 관리자 API ---
    @GetMapping("/api/core/admin/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // --- 내 정보 API ---
    // user-service의 /users/me/reviews가 이 API를 호출하게 됩니다.
    @GetMapping("/api/core/users/me/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews() { // ✨ 파라미터 수정
        List<ReviewResponseDto> reviews = reviewService.findMyReviews(); // ✨ 파라미터 수정
        return ResponseEntity.ok(reviews);
    }
}