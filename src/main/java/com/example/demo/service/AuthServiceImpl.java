package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.helper.JwtHelper;
import com.example.demo.model.JwtResponse;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtHelper jwtHelper;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public Object register(Users user, HttpServletResponse response) {
		Users userDB = userRepository.findByUsername(user.username);
		if (userDB != null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "user already exist";
		}

		user.password = passwordEncoder.encode(user.password);
		
		userRepository.save(user);
		response.setStatus(HttpServletResponse.SC_CREATED);
		return "success";
	}

	@Override
	public Object login(Users user, HttpServletResponse response) {
		if (user.password == null || user.password.equalsIgnoreCase("")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "Password tidak boleh kosong";
		}
		
		Users userDB = userRepository.findByUsername(user.username);
		
		if (userDB == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "User tidak terdaftar";
		}
	
		Boolean success = passwordEncoder.matches(user.password, userDB.password);
		
		if (!success) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "Password Salah";
		}
		
		String token = jwtHelper.generateToken(user.username);
        return new JwtResponse(token);
	}

	@Override
	public Object validateToken(String authorizationHeader) {
		return "Success";
	}

}
