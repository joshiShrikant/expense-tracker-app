package com.jts.expensetracker.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String role; // e.g., "USER", "ADMIN"
    private boolean enabled = true;
}
