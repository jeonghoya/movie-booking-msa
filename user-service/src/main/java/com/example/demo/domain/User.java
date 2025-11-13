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
@Table(name = "users") // <-- 바로 이 줄을 추가해 주세요!
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;


    // 예매 목록과의 관계 (1:N)
    @JsonIgnore // <-- 이 어노테이션을 추가하세요!
    @OneToMany(mappedBy = "user")
    private transient List<Booking> bookings = new ArrayList<>();

    // --- ✨ 아래 필드를 새로 추가합니다. ---
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) // Enum 값을 DB에 저장할 때, Enum의 이름(STRING)을 저장하도록 설정
    private UserRoleEnum role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private transient List<Review> reviews = new ArrayList<>();

    // ✨ 연관관계 편의 메소드 추가
    public void addReview(Review review) {
        this.reviews.add(review);
        review.setUser(this);
    }
}