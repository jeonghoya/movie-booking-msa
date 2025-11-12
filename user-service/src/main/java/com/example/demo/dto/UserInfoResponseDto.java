package com.example.demo.dto;

import com.example.demo.domain.User;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private Long id;
    private String email;
    private String username;

    // Entity를 DTO로 변환하는 생성자
    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }
}
