package com.jts.expensetracker.auth.controller;

import com.jts.expensetracker.auth.dto.LoginRequest;
import com.jts.expensetracker.auth.dto.LoginResponse;
import com.jts.expensetracker.auth.dto.RegisterRequest;
import com.jts.expensetracker.auth.service.JwtService;
import com.jts.expensetracker.model.User;
import com.jts.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // adjust for production
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

            String token = jwtService.generateToken(user);

            UserDetails userResponse = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password("********")
                    .roles(user.getAuthorities().stream()
                            .map(auth -> auth.getAuthority().replace("ROLE_", "")) // removes ROLE_ prefix
                            .toArray(String[]::new))
                    .disabled(!user.isEnabled())
                    .build();

            return ResponseEntity.ok(new LoginResponse(token, userResponse));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("Register endpoint hit");
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole("USER");
        user.setEnabled(true);
        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password("********")
                .roles(user.getRole())
                .disabled(!user.isEnabled())
                .build();

        String token = jwtService.generateToken(userDetails);

        LoginResponse response = new LoginResponse(token, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserDetails(@PathVariable String username) {
        return userRepository.findByUsername(username).map(user -> {
            return ResponseEntity.ok(user);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.ok("User is logged in as: " + authentication.getName());
        } else {
            return ResponseEntity.status(401).body("User is not logged in");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!user.isEnabled())
                .build();

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", newAccessToken);
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return ResponseEntity.ok(tokens);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return userRepository.findByUsername(username).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok("User deleted successfully");
        }).orElse(ResponseEntity.notFound().build());
    }
}