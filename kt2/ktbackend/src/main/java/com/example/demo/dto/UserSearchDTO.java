package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchDTO {
    private String firstName;
    private String lastName;
    private String email;
}
