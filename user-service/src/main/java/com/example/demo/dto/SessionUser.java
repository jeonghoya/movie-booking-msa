package com.example.demo.dto; // 본인의 패키지 경로

import com.example.demo.domain.User;
import com.example.demo.domain.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// 이 DTO는 가벼우며, User Entity와 달리 복잡한 관계(List<Booking> 등)가 없습니다.
// ✨ UserDetails를 구현해야 Spring Security가 이 객체를 '사용자'로 인식합니다.
public class SessionUser implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final UserRoleEnum role;

    // 생성자: User Entity를 받아서 가벼운 SessionUser DTO를 만듭니다.
    public SessionUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword(); // ✨ 중요: Security는 비밀번호를 비교해야 함
        this.role = user.getRole();
    }

    // ✨ Service 로직에서 ID를 꺼내 쓸 수 있도록 Getter 추가
    public Long getId() {
        return id;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    // --- UserDetails 인터페이스의 필수 메소드들 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 역할(Role)을 Spring Security가 인식할 수 있는 GrantedAuthority로 변환
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        // ✨ 우리는 ID로 email을 사용하므로 email을 반환
        return this.email;
    }

    // (이하 4개 메소드는 지금 프로젝트에선 중요하지 않으므로 true로 반환)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}