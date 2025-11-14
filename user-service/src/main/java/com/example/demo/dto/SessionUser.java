package com.example.demo.dto;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor                     // ✅ Redis 역직렬화에 필요
@JsonIgnoreProperties(ignoreUnknown = true)   // ✅ 모르는 필드는 무시 (enabled 같은 것 때문에 안 터지게)
public class SessionUser implements UserDetails, Serializable {

    private Long id;
    private String email;
    private String password;
    private UserRoleEnum role;
    private List<GrantedAuthority> authorities;

    // 🔥 에러 원인 해결용 필드
    // Redis에 이미 저장된 JSON 안에 "enabled": true 가 있어서 이 필드가 필요함
    private boolean enabled = true;

    public SessionUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name())
        );
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

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
        return enabled;
    }

    // 🔥 Jackson 이 "enabled" 값을 매핑할 수 있도록 setter도 하나 만들어 줌
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
