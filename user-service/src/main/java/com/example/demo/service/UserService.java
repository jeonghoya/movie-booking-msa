package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRoleEnum;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.SessionUser;
import com.example.demo.dto.UserInfoResponseDto;
import com.example.demo.dto.UserSignUpRequestDto;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private final JwtUtil jwtUtil; // ✨ JWT를 사용하지 않으므로 삭제

    public User signUp(UserSignUpRequestDto requestDto) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // DTO를 Entity로 변환
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());
        // 비밀번호는 반드시 암호화해서 저장
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(UserRoleEnum.USER);

        return userRepository.save(user);
    }

    // ✨ login 메소드 전체 삭제
    // 이유: 로그인 인증은 SecurityConfig에 등록된 AuthenticationManager를 통해
    // UserController에서 직접 처리됩니다. UserService는 더 이상 로그인 로직에 관여하지 않습니다.
    /*
    public String login(LoginRequestDto requestDto) {
        // ... (이 메소드 전체를 삭제)
    }
    */

    // --- '내 정보 조회' 메소드 ---
    public UserInfoResponseDto getUserInfo(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 1. SecurityContext에서 SessionUser DTO를 직접 꺼냅니다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SessionUser sessionUser = (SessionUser) principal;

        // 2. DTO의 ID로 DB에서 User Entity를 조회합니다.
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserInfoResponseDto(user);
    }
}
