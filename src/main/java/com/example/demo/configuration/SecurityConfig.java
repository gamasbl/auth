package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.helper.JwtFilterHelper;

@Configuration
public class SecurityConfig {

	@Bean
    public JwtFilterHelper jwtFilterHelper() {
        return new JwtFilterHelper();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll() // Endpoint yang tidak memerlukan JWT
                .anyRequest().authenticated() // Endpoint lain memerlukan JWT
            .and()
            .addFilterBefore(jwtFilterHelper(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Menggunakan BCrypt untuk hashing password
        return new BCryptPasswordEncoder();
    }
}
