package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.helper.JwtHelper;
import com.example.demo.model.JwtResponse;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class Controller {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/api/auth/register")
	public Object register(@RequestBody Users user, HttpServletResponse response) {
		return authService.register(user, response);
	}
	
	@PostMapping("/api/auth/login")
	public Object login(@RequestBody Users user, HttpServletResponse response) {
        return authService.login(user, response);
	}
	
	@GetMapping("/api/validate")
	public Object validateToken(@RequestHeader("Authorization") String authorizationHeader) {
		String token = authorizationHeader.substring(7);
		return authService.validateToken(token);
	}

}
