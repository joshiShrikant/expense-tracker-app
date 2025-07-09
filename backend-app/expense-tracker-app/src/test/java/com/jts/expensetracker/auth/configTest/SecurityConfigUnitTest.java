package com.jts.expensetracker.auth.configTest;

import com.jts.expensetracker.auth.config.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigUnitTest {

    private final SecurityConfig securityConfig = new SecurityConfig(null, null) {
        @Override
        public PasswordEncoder passwordEncoder() {
            return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        }

        @Override
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:4200"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
    };

    @Test
    void passwordEncoderShouldEncodePassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testCorsConfiguration() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(HttpServletRequest request) {;
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                return super.getCorsConfiguration(request);
            }
        };

        assertNotNull(config);
        assertTrue(config.getAllowedOrigins().contains("http://localhost:4200"));
        assertTrue(config.getAllowedMethods().containsAll(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")));
        assertTrue(config.getAllowCredentials());
    }
}