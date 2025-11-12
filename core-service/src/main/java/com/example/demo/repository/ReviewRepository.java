// src/main/java/com/example/demo/repository/ReviewRepository.java
package com.example.demo.repository;

import com.example.demo.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 영화 ID로 해당 영화의 모든 리뷰를 찾아 반환
    List<Review> findByMovieId(Long movieId);
    List<Review> findByUserId(Long userId); // ✨ 이 메소드 추가
}