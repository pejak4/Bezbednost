package com.example.demo.view;

public class UserLoginView {

    private String email;
    private String password;

    public UserLoginView() {
        super();
    }

    public UserLoginView(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
