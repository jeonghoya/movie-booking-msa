package com.example.demo.domain;

public enum UserRoleEnum {
    USER,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();   // ROLE_USER, ROLE_ADMIN 형태
    }
}
