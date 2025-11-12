package com.example.demo.repository;

import com.example.demo.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByScreeningId(Long screeningId); // ✨ 이 메소드 추가
}