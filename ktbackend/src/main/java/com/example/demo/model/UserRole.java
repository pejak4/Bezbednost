package com.example.demo.model;

import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public enum UserRole {

    ADMIN("ADMIN"), USER("USER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }
}
