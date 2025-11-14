//package com.example.demo.domain;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class Movie {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String title;
//
//    private String director;
//    private String genre;
//    private int runningTime; // 분 단위
//
//    // 아래 두 줄을 추가하여 무한 루프를 방지합니다.
//    @JsonIgnore
//    @OneToMany(mappedBy = "movie")
//    private List<Booking> bookings = new ArrayList<>();
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews = new ArrayList<>();
//
//    // ✨ 연관관계 편의 메소드 추가
//    public void addReview(Review review) {
//        this.reviews.add(review);
//        review.setMovie(this);
//    }
//}

// src/main/java/com/example/demo/domain/Movie.java

package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    //private static final long serialVersionUID = 1L; // ✨ 이 줄 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String director;
    private String genre;
    private int runningTime;

    // --- ✨ 이 부분을 수정합니다 ---
    // Movie는 이제 Booking이 아닌 Screening과 직접 관계를 맺습니다.
    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Screening> screenings = new ArrayList<>();
    // --- 여기까지 ---

    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setMovie(this);
    }
}
