package com.mysite.sbb.user;


import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
        USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
// 상수 자료형이므로 @Setter없이 @Getter만 사용가능
