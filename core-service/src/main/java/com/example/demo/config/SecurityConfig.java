package com.example.demo.config;
//
//import com.example.demo.filter.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authz -> authz
//                        // 1. [PUBLIC] 인증 없이 누구나 접근 가능한 경로
//                        .requestMatchers("/", "/index.html", "/*.html", "/css/**", "/js/**").permitAll()
//                        .requestMatchers("/users/signup", "/users/login").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/movies", "/movies/**").permitAll()
//                        // ✨ 이 부분을 수정합니다. screenings 관련 GET 요청을 모두 permitAll()로 명시합니다.
//                        .requestMatchers(HttpMethod.GET, "/movies/{movieId}/screenings", "/screenings/**", "/screening-halls").permitAll()
//
//                        // 2. [ADMIN] 관리자만 접근 가능한 경로
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        // ✨ [ADMIN] 규칙에 screenings 경로의 POST, DELETE 추가
//                        .requestMatchers(HttpMethod.POST, "/movies", "/screenings").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/movies/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/movies/**", "/screenings/**").hasRole("ADMIN")

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

////                        .requestMatchers(HttpMethod.POST, "/movies").hasRole("ADMIN")
////                        .requestMatchers(HttpMethod.PUT, "/movies/**").hasRole("ADMIN")
////                        .requestMatchers(HttpMethod.DELETE, "/movies/**").hasRole("ADMIN")
//
//                        // 3. [USER & ADMIN] 인증된 사용자만 접근 가능한 경로
//                        .requestMatchers("/users/me", "/users/logout", "/users/me/reviews").authenticated()
//                        .requestMatchers("/bookings/**").authenticated()
//                        .requestMatchers("/reviews/**").authenticated()
//                        .requestMatchers("/movies/{movieId}/reviews").authenticated()
//
//                        // 4. 나머지 모든 요청은 거부
//                        .anyRequest().denyAll()
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}

// core-service/config/SecurityConfig.java

// import com.example.demo.filter.JwtAuthenticationFilter; // ✨ 삭제
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy; // ✨ 삭제
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // ✨ 삭제

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // private final JwtAuthenticationFilter jwtAuthenticationFilter; // ✨ 삭제

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // ✨ STATELESS (상태 없음) 설정을 삭제 -> 기본값인 STATEFUL (세션 사용)으로 변경
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authz -> authz
                        // 1. [PUBLIC] 비인증 사용자도 접근 가능한 경로 (조회 기능)
                        .requestMatchers(HttpMethod.GET,
                                "/api/core/movies",
                                "/api/core/movies/**",
                                "/api/core/screenings/**",
                                "/api/core/screening-halls"
                        ).permitAll()

                        // 2. [ADMIN] 관리자만 접근 가능한 경로
                        .requestMatchers("/api/core/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/core/movies", "/api/core/screenings").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/core/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/core/movies/**", "/api/core/screenings/**").hasRole("ADMIN")

                        // 3. [USER & ADMIN] 인증된 사용자만 접근 가능한 경로
                        .requestMatchers("/api/core/bookings/**").authenticated()
                        .requestMatchers("/api/core/reviews/**").authenticated()
                        .requestMatchers("/api/core/movies/{movieId}/reviews").authenticated()

                        // 4. 나머지 모든 요청은 거부 (혹은 authenticated()도 가능)
                        .anyRequest().denyAll()
                );

        // ✨ JWT 필터 등록 라인 삭제
        // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}