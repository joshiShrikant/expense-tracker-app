package com.jts.expensetracker.auth.configTest;

import com.jts.expensetracker.auth.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the required beans
    @MockBean
    private JwtAuthenticationFilter jwtAuthFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Should allow public access to /api/auth/login")
    void shouldAllowPublicAccessToLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login"))
                .andExpect(status().isOk()); // or is4xx depending on actual controller setup
    }

    @Test
    @DisplayName("Should block unauthorized access to secured endpoint")
    void shouldBlockUnauthorizedAccessToSecuredEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/secure/data"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 404 for unknown endpoints")
    void shouldReturn404ForUnknownEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/some/nonexistent/route"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Should load CORS configuration")
    void shouldAllowCorsFromFrontend() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/api/auth/login")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://localhost:4200"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"))
                .andExpect(status().isOk());
    }
}

