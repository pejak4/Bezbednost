package com.example.demo.view;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterView {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;
}
