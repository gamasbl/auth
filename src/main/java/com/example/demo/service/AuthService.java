package com.example.demo.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.model.Users;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	Object register(@RequestBody Users user, HttpServletResponse response);
	Object login(@RequestBody Users user, HttpServletResponse response);
	Object validateToken(@RequestHeader("Authorization") String authorizationHeader);
}
