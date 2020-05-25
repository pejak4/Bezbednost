package com.example.demo.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenState {

    private String accessToken;
    private Long expiresIn;
    private boolean firstTimeLogged = true;
}
