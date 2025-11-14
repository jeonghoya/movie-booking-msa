//package com.example.demo.domain;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class Booking {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne // N:1 관계
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne // N:1 관계
//    @JoinColumn(name = "movie_id")
//    private Movie movie;
//
//    private LocalDateTime bookingTime; // 예매 시간
//    private int ticketCount; // 예매 인원
//    private String seatInfo; // 좌석 정보 (예: "A1,A2")
//
//    // 추후 Payment와 1:1 관계 설정 가능
//}

// src/main/java/com/example/demo/domain/Booking.java
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    //private static final long serialVersionUID = 1L; // ✨ 이 줄 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // ✨ Movie -> Screening 으로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    // ✨ ticketCount 삭제, seats 리스트 추가
    @ElementCollection(fetch = FetchType.EAGER) // 좌석 정보는 예매와 함께 바로 조회
    @CollectionTable(name = "booked_seats", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "seat_coordinate") // 예: "A1", "C5"
    private List<String> seats = new ArrayList<>();
}