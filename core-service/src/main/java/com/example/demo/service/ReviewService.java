// src/main/java/com/example/demo/service/ReviewService.java
package com.example.demo.service;

import com.example.demo.domain.Movie;
import com.example.demo.domain.Review;
import com.example.demo.domain.User;
import com.example.demo.domain.UserRoleEnum;
import com.example.demo.dto.ReviewRequestDto;
import com.example.demo.dto.ReviewResponseDto;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

//    private final ReviewRepository reviewRepository;
//    private final UserRepository userRepository;
//    private final MovieRepository movieRepository;
//
//    // 특정 영화에 대한 리뷰 작성
//    @Transactional
//    public void createReview(String email, Long movieId, ReviewRequestDto requestDto) {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
//        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("영화 없음"));
//
//        Review review = new Review();
//        //review.setUser(user);
//        //review.setMovie(movie);
//        review.setContent(requestDto.getContent());
//        review.setRating(requestDto.getRating());
//        movie.addReview(review);
//        user.addReview(review);
//
//
//        reviewRepository.save(review);
//    }
//
//    //(수정/삭제 기능은 Controller를 만든 후, 다음 단계에서 추가하겠습니다.)
//    // --- ✨ 아래 수정 및 삭제 메소드를 새로 추가합니다. ---
//
//    // ✨ 리뷰 수정 메소드 수정
//    @Transactional
//    public void updateReview(Long reviewId, User user, ReviewRequestDto requestDto) {
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));
//
//        // ✨ 권한 확인 로직 변경
//        // 리뷰 작성자가 아니고, 관리자도 아닐 경우에만 예외 발생
//        if (!review.getUser().getId().equals(user.getId()) && user.getRole() != UserRoleEnum.ADMIN) {
//            throw new IllegalStateException("본인의 리뷰만 수정할 수 있습니다.");
//        }
//
//        review.setContent(requestDto.getContent());
//        review.setRating(requestDto.getRating());
//    }
//
//    // ✨ 리뷰 삭제 메소드 수정
//    @Transactional
//    public void deleteReview(Long reviewId, User user) {
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));
//
//        // ✨ 권한 확인 로직 변경
//        // 리뷰 작성자가 아니고, 관리자도 아닐 경우에만 예외 발생
//        if (!review.getUser().getId().equals(user.getId()) && user.getRole() != UserRoleEnum.ADMIN) {
//            throw new IllegalStateException("본인의 리뷰만 삭제할 수 있습니다.");
//        }
//
//        reviewRepository.delete(review);
//    }
//
//    // ✨ 내가 쓴 리뷰 조회 메소드 추가
//    @Transactional(readOnly = true)
//    public List<ReviewResponseDto> getMyReviews(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
//        return reviewRepository.findByUserId(user.getId()).stream()
//                .map(ReviewResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    // ✨ 관리자용 전체 리뷰 조회 메소드 추가
//    @Transactional(readOnly = true)
//    public List<ReviewResponseDto> getAllReviews() {
//        return reviewRepository.findAll().stream()
//                .map(ReviewResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    // 특정 영화의 모든 리뷰 조회
//    @Transactional(readOnly = true)
//    public List<ReviewResponseDto> getReviewsForMovie(Long movieId) {
//        List<Review> reviews = reviewRepository.findByMovieId(movieId);
//        return reviews.stream()
//                .map(ReviewResponseDto::new)
//                .collect(Collectors.toList());
//    }
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository; // ✨ DB를 공유하므로 UserRepository 유지
    private final MovieRepository movieRepository;

    @Transactional
    public Review createReview(Long movieId, ReviewRequestDto requestDto) { // ✨ 파라미터 수정
        // ✨ SecurityContext에서 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다."));

        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setContent(requestDto.getContent());
        review.setRating(requestDto.getRating());

        // ✨ 양방향 연관관계 편의 메소드 호출 (모놀리식 코드 기반)
        movie.addReview(review);
        user.addReview(review);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long reviewId, ReviewRequestDto requestDto) { // ✨ 파라미터 수정
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("본인의 리뷰만 수정할 수 있습니다.");
        }

        review.setContent(requestDto.getContent());
        review.setRating(requestDto.getRating());
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) { // ✨ 파라미터 수정
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // ✨ 관리자(ADMIN)도 삭제할 수 있도록 권한 확인
        if (!review.getUser().getId().equals(user.getId()) && user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalStateException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findMyReviews() { // ✨ 파라미터 수정
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return reviewRepository.findByUserId(user.getId()).stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // --- (이하 메소드들은 수정 불필요) ---

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsForMovie(Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return reviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }
}

