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

// user-service/config/SecurityConfig.java

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // private final JwtAuthenticationFilter jwtAuthenticationFilter; // ✨ 삭제

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    // ✨ AuthenticationManager를 Bean으로 등록 (로그인 시 사용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf.disable())
//                // ✨ STATELESS (상태 없음) 설정을 삭제 -> 기본값인 STATEFUL (세션 사용)으로 변경
//                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/api/user/signup", "/api/user/login").permitAll()
//                        // ... (기타 user-service의 public 경로)
//                        .anyRequest().authenticated()
//                )
                .csrf(csrf -> csrf.disable())
                // 세션 방식(STATEFUL)을 사용하므로 .sessionManagement(...) 설정 불필요 (기본값)

                .authorizeHttpRequests(authz -> authz
                        // 1. [PUBLIC] 인증 없이 누구나 접근 가능한 경로
                        // ✨ 4-2.5단계 수정 사항 반영 ✨
                        .requestMatchers("/api/user/signup", "/api/user/login").permitAll()

                        // 2. [USER & ADMIN] 인증된 사용자만 접근 가능한 경로
                        // ✨ 4-2.5단계 수정 사항 반영 (이 부분이 제가 언급했던 그 줄입니다) ✨
                        .requestMatchers("/api/user/me", "/api/user/logout").authenticated()

                        // 3. 나머지 모든 요청은 거부
                        .anyRequest().denyAll()
                );

                // ✨ JWT 필터 등록 라인 삭제
                // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//                // ✨ 세션 방식의 로그아웃 기능 추가
//                .logout(logout -> logout
//                        .logoutUrl("/users/logout") // 로그아웃을 처리할 URL
//                        .logoutSuccessUrl("/login.html?logout=true") // 로그아웃 성공 시 리디렉션
//                        .deleteCookies("SESSION") // Redis에 저장된 세션 쿠키 삭제
//                        .invalidateHttpSession(true) // 세션 무효화
//                );

        return http.build();
    }
}