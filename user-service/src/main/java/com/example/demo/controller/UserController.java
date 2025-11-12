package com.example.demo.controller;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserInfoResponseDto;
import com.example.demo.dto.UserSignUpRequestDto;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager; // ✨ 주입

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    // 로그인 API 수정
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

        // 1. Spring Security의 인증 매니저를 통해 인증 수행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        // 2. 인증이 성공하면, SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Spring Session이 세션을 강제로 생성하고 Redis에 저장하도록 함
        // 이 코드가 실행되면, 클라이언트에게 "SESSION" 쿠키가 발급됨.
        request.getSession(true);

        // 4. JWT 토큰 대신 간단한 성공 메시지 반환
        return ResponseEntity.ok("로그인 성공");
    }

    // ✨ 기존 getMyInfo 메소드를 아래 코드로 수정합니다.
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserInfoResponseDto userInfo = userService.getUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(userInfo);
    }

    // --- 아래 로그아웃 메소드를 새로 추가합니다. ---
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 실제로는 클라이언트 측에서 토큰을 삭제하는 것이 핵심입니다.
        // 서버에서는 특별히 할 일이 없으므로, 성공 응답만 반환합니다.
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
