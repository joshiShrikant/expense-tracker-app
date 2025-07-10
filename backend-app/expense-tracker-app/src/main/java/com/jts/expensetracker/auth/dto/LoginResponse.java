package com.jts.expensetracker.auth.dto;

import com.jts.expensetracker.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private UserDetails user;

    public LoginResponse(String token, UserDetails user) {
        this.user = user;
        this.token = token;
    }
}
