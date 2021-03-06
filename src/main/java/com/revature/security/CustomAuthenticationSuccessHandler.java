package com.revature.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.revature.security.jwt.JwtConstants;
import com.revature.security.jwt.JwtTokenUtil;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override 
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.err.println("Inside CustomAuthenticationSuccessHandler...");
		final String token = JwtConstants.TOKEN_PREFIX + jwtTokenUtil.generateToken(authentication);
		System.err.println("Value of token: " + token);
		response.addHeader(JwtConstants.HEADER_STRING, token);
		response.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		String uri = "";
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
			uri = "/test-feign";
		else if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")))
			uri = "/test-feign-user";
		request.getRequestDispatcher("/application-service" + uri).forward(request, response);
	}

}
