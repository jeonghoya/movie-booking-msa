// src/main/java/com/example/demo/domain/ScreeningHall.java
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScreeningHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 예: "1관", "IMAX관"

    @Column(nullable = false)
    private int totalRows; // 총 행 수

    @Column(nullable = false)
    private int totalColumns; // 총 열 수
}