package com.example.demo.helper;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;

@Component
public class JwtFilterHelper extends OncePerRequestFilter{

	@Autowired
    private JwtHelper jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        try {
        	if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwtToken);
            }
            
        	if (username != null && jwtUtil.validateToken(jwtToken, username)) {
                // Membuat UsernamePasswordAuthenticationToken dan memasukkan ke SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.emptyList());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set ke SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (SignatureException e) {
        	// Token signature tidak cocok
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Invalid token signature.");
            return;
        } catch (ExpiredJwtException e) {
            // Token expired
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Token expired.");
            return;
        }

        filterChain.doFilter(request, response);
    }
    
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //endpoint yang tidak perlu validasi JWT
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/**");  // misalnya, tidak perlu token di /public atau /login
    }
}
