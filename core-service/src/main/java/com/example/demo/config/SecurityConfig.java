package com.example.demo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(authz -> authz

                        // 0. 디버그/에러는 누구나 접근 가능
                        .requestMatchers("/api/core/debug/**", "/error").permitAll()

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
                        .requestMatchers("/api/core/users/me/reviews").authenticated() // ✨ 이 줄 추가

                        // 4. 나머지 모든 요청은 거부 (혹은 authenticated()도 가능)
                        .anyRequest().denyAll()
                );
        //http.userDetailsService(customUserDetailsService);  // 🔥 중요

        // ✨ JWT 필터 등록 라인 삭제
        // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}