package com.example.demo.dto;

import com.example.demo.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String content;
    private int rating;
    private String username;
    private ReviewMovieInfoDto movieInfo; // ✨ 기존 Movie 필드를 이 DTO로 변경

    // ✨ 영화 정보를 담을 간단한 내부 DTO 클래스
    @Getter
    private static class ReviewMovieInfoDto {
        private Long id;
        private String title;

        public ReviewMovieInfoDto(Long id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.username = review.getUser().getUsername();
        // ✨ 생성자에서 Movie 객체 전체가 아닌, 필요한 정보만 뽑아서 DTO를 생성
        this.movieInfo = new ReviewMovieInfoDto(review.getMovie().getId(), review.getMovie().getTitle());
    }
}