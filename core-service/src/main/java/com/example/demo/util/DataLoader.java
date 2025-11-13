//package com.example.demo.util;
//
//import com.example.demo.domain.Movie;
//import com.example.demo.repository.MovieRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component // 이 클래스를 Spring이 관리하는 Bean으로 등록
//@RequiredArgsConstructor
//public class DataLoader implements CommandLineRunner {
//
//    private final MovieRepository movieRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 애플리케이션 시작 시점에 실행될 코드
//
//        // 기존 데이터가 없으면 샘플 데이터 추가
//        if (movieRepository.count() == 0) {
//            Movie movie1 = new Movie();
//            movie1.setTitle("탑건: 매버릭");
//            movie1.setDirector("조셉 코신스키");
//            movie1.setGenre("액션");
//            movie1.setRunningTime(130);
//
//            Movie movie2 = new Movie();
//            movie2.setTitle("아바타: 물의 길");
//            movie2.setDirector("제임스 카메론");
//            movie2.setGenre("SF");
//            movie2.setRunningTime(192);
//
//            movieRepository.save(movie1);
//            movieRepository.save(movie2);
//
//            System.out.println("테스트용 영화 데이터가 추가되었습니다.");
//        }
//    }
//}
package com.example.demo.util;

import com.example.demo.domain.*;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ScreeningHallRepository;
import com.example.demo.repository.ScreeningRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository; // ✨ UserRepository 주입
    private final PasswordEncoder passwordEncoder; // ✨ PasswordEncoder 주입
    private final ScreeningHallRepository screeningHallRepository; // ✨ Repository 주입
    private final ScreeningRepository screeningRepository;     // ✨ Repository 주입

    @Override
    public void run(String... args) throws Exception {
        // --- 테스트용 사용자 데이터 추가 ---
        if (userRepository.count() == 0) {
            // ADMIN 계정 생성
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setUsername("관리자");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRoleEnum.ADMIN);
            userRepository.save(admin);

            // USER 계정 생성
            User user = new User();
            user.setEmail("user@example.com");
            user.setUsername("일반사용자");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(UserRoleEnum.USER);
            userRepository.save(user);
        }

        // --- 테스트용 영화 데이터 추가 ---
        if (movieRepository.count() == 0) {
            Movie movie1 = new Movie();
            movie1.setTitle("탑건: 매버릭");
            movie1.setDirector("조셉 코신스키");
            movie1.setGenre("액션");
            movie1.setRunningTime(130);

            Movie movie2 = new Movie();
            movie2.setTitle("아바타: 물의 길");
            movie2.setDirector("제임스 카메론");
            movie2.setGenre("SF");
            movie2.setRunningTime(192);

            Movie movie3 = new Movie();
            movie3.setTitle("기생충");
            movie3.setDirector("봉준호");
            movie3.setGenre("SF");
            movie3.setRunningTime(180);

            movieRepository.save(movie1);
            movieRepository.save(movie2);
            movieRepository.save(movie3);

            System.out.println("테스트용 영화 데이터 및 테스트용 관리자 사용자 계정이 추가되었습니다.");
        }

        // --- ✨ 테스트용 상영관 및 상영 정보 추가 ---
        if (screeningRepository.count() == 0) {
            // 상영관 2개 생성
            ScreeningHall hall1 = new ScreeningHall();
            hall1.setName("1관");
            hall1.setTotalRows(10); // 10행
            hall1.setTotalColumns(10); // 10열
            screeningHallRepository.save(hall1);

            ScreeningHall hall2 = new ScreeningHall();
            hall2.setName("2관 (IMAX)");
            hall2.setTotalRows(8); // 8행
            hall2.setTotalColumns(12); // 12열
            screeningHallRepository.save(hall2);

            // 영화 1번("탑건: 매버릭")에 대한 상영 정보 2개 생성
            Movie movie1 = movieRepository.findById(1L).orElse(null);
            if (movie1 != null) {
                Screening screening1 = new Screening();
                screening1.setMovie(movie1);
                screening1.setScreeningHall(hall1);
                screening1.setScreeningTime(LocalDateTime.now().plusHours(2)); // 현재 시간 기준 2시간 뒤
                screeningRepository.save(screening1);

                Screening screening2 = new Screening();
                screening2.setMovie(movie1);
                screening2.setScreeningHall(hall2);
                screening2.setScreeningTime(LocalDateTime.now().plusHours(3)); // 현재 시간 기준 3시간 뒤
                screeningRepository.save(screening2);
            }

            // --- ✨ 아래 내용을 새로 추가합니다 ---
            // 영화 2번("아바타: 물의 길")에 대한 상영 정보 생성
            Movie movie2 = movieRepository.findById(2L).orElse(null);
            if (movie2 != null && hall1 != null) {
                Screening screening3 = new Screening();
                screening3.setMovie(movie2);
                screening3.setScreeningHall(hall1);
                screening3.setScreeningTime(LocalDateTime.now().plusHours(4));
                screeningRepository.save(screening3);
            }

            // 영화 3번("기생충")에 대한 상영 정보 생성
            Movie movie3 = movieRepository.findById(3L).orElse(null);
            if (movie3 != null && hall2 != null) {
                Screening screening4 = new Screening();
                screening4.setMovie(movie3);
                screening4.setScreeningHall(hall2);
                screening4.setScreeningTime(LocalDateTime.now().plusHours(5));
                screeningRepository.save(screening4);
            }
        }
    }
}
