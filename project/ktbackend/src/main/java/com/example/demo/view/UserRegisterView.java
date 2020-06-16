package com.example.demo.view;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterView {

    @NotNull(message = "First name can't be null")
    private String firstName;

    @NotNull(message = "Last name can't be null")
    private String lastName;

    @NotNull(message = "Email can't be null")
    @Email(message = "Put a valid email")
    private String email;

    @NotNull(message = "Password can't be null")
    @Size(min=10, message = "Password must be equal or greater then 10 characters, to have capital letter and special character")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$")
    private String password;

    @NotNull(message = "Password can't be null")
    @Size(min=10, message = "Password must be equal or greater then 10 characters, to have capital letter and special character")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$")
    private String repeatPassword;
}
