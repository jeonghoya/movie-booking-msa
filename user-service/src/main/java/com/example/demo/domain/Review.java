// src/main/java/com/example/demo/domain/Review.java
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {

    //private static final long serialVersionUID = 1L; // ✨ 이 줄 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; // 리뷰 내용

    @Column(nullable = false)
    private int rating; // 별점 (1~5)

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 최적화
    @JoinColumn(name = "user_id")
    private User user; // 리뷰를 작성한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie; // 리뷰가 달린 영화
}