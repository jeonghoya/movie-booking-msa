package com.example.demo.repository;

import com.example.demo.domain.Screening;
import com.example.demo.domain.ScreeningHall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovieId(Long movieId);
    // --- ✨ 이 메소드를 새로 추가합니다 ---
    List<Screening> findByScreeningHall(ScreeningHall screeningHall);
}