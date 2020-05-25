package com.example.demo.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class emailDTO {

//    @NotNull(message = "Email can't be null")
//    @Email
    private String email;
}
