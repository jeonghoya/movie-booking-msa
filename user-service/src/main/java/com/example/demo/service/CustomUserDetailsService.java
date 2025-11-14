package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

//        // ✨ 사용자의 역할을 GrantedAuthority로 변환합니다.
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
//        // ✨ 중요: Spring Security는 역할 이름 앞에 항상 "ROLE_" 접두사가 붙어있다고 가정합니다.
//
//        // ✨ 기존 new ArrayList<>() 대신, 방금 만든 authorities 리스트를 넣어줍니다.
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
        // ✨ User Entity 대신 가벼운 SessionUser DTO를 반환합니다.
        // ✨ (SessionUser.java 파일은 이미 준비되어 있습니다)
        return new com.example.demo.dto.SessionUser(user);

//        com.example.demo.domain.User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
//        // 권한 리스트 생성 (ROLE_USER / ROLE_ADMIN)
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
//
//        // 스프링 시큐리티 내장 User 사용
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                authorities
//        );
    }
}