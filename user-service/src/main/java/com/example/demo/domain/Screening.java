// src/main/java/com/example/demo/domain/Screening.java
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Screening implements Serializable {

    private static final long serialVersionUID = 1L; // ✨ 이 줄 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_hall_id")
    private ScreeningHall screeningHall;

    @Column(nullable = false)
    private LocalDateTime screeningTime; // 상영 시작 시간
}