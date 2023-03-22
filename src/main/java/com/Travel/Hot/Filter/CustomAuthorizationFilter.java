package com.Travel.Hot.Filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CustomAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if(request.getServletPath().equals("/login") || request.getServletPath().equals("/token/refresh")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					
					String token = authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username = decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					for(String role: roles)
						authorities.add(new SimpleGrantedAuthority(role));
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} catch (Exception e) {
					System.out.println("Erreur de connexion dans: "+e.getMessage());
					response.setHeader("erreur ", e.getMessage());
					response.setStatus(403);
					//response.sendError(403);
					Map<String, String> error = new HashMap<>();
					error.put("error_message", e.getMessage()); 
					response.setContentType("application/json");
					new ObjectMapper().writeValue(response.getOutputStream(), error);				
				}
				
			}else
				filterChain.doFilter(request, response);
		}

	}

}
