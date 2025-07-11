package com.jts.expensetracker.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {
    String generateToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean validateToken(String token);

    String generateRefreshToken(UserDetails userDetails);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
