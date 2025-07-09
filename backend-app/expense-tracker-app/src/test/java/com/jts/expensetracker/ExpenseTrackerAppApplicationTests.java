package com.jts.expensetracker;

import com.jts.expensetracker.auth.controller.AuthController;
import com.jts.expensetracker.auth.service.JwtService;
import com.jts.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExpenseTrackerAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private UserDetailsService userDetailsService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void whenPublicEndpoint_thenAllowAccess() throws Exception {
		mockMvc.perform(post("/api/auth/register")
						.contentType("application/json")
						.content("{\"username\":\"test\",\"password\":\"test\",\"email\":\"test@example.com\"}"))
				.andExpect(status().isOk());
	}

	@Test
	void testCorsConfiguration() throws Exception {
		mockMvc.perform(post("/api/auth/register")
						.header("Origin", "http://localhost:4200")
						.contentType("application/json")
						.content("{\"username\":\"test\",\"password\":\"test\",\"email\":\"test@example.com\"}"))
				.andExpect(status().isOk());
	}
}