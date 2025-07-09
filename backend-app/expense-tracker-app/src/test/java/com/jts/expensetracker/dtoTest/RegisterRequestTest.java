package com.jts.expensetracker.dtoTest;

import com.jts.expensetracker.auth.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void testRegisterRequestCreation() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("test@example.com");
        request.setRole("USER");
        request.setEnabled(true);

        assertEquals("testuser", request.getUsername());
        assertEquals("password123", request.getPassword());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("USER", request.getRole());
        assertTrue(request.isEnabled());
    }

    @Test
    void testDefaultEnabled() {
        RegisterRequest request = new RegisterRequest();
        assertTrue(request.isEnabled(), "Enabled should be true by default");
    }
}