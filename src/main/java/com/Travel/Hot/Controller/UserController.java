package com.Travel.Hot.Controller;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.Travel.Hot.Service.UserService;
import com.Travel.Hot.domain.Role;
import com.Travel.Hot.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;


@Controller
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/list")
	public  ResponseEntity<Object> getUsers() {
		return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
	}
	
	@GetMapping("/recherche/{idUser}")
	public ResponseEntity<Object>  FindUsers(@PathVariable("idUser") Long idUser) {
		return new ResponseEntity<>(userService.getUser(idUser), HttpStatus.OK);
	}
	
	@PostMapping("/newUser")
	public ResponseEntity<User> NewUsers(@RequestBody User user) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/newUser").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
	}
	
	@PostMapping("/newRole")
	public ResponseEntity<Role> NewRoles(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/newRole").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	@GetMapping("/token/refresh")
	public void RefreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refresh_token = authorizationHeader.substring("Bearer".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				User user = userService.FindUserByEmail(username);
				List<Role> roles = new ArrayList<>();
				roles.add(user.getRoles());
				String acces_token = JWT.create()
						.withSubject(user.getEmail())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", roles.stream().map(Role::getNomRole).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("acces_token", acces_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);				
			} catch (Exception e) {
				response.setHeader("erreur ", e.getMessage());
				response.setStatus(403);
				//response.sendError(403);
				Map<String, String> error = new HashMap<>();
				error.put("error_message", e.getMessage()); 
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), error);				
			}
			
		}else
			throw new RuntimeException("Refresh token is missing");
	}
	
	
	@Data
	class RoleToUserForm{
		private Long idUser;
		private Long idRole;
		
		public Long getIdUser() {
			return idUser;
		}
		public void setIdUser(Long idUser) {
			this.idUser = idUser;
		}
		public Long getIdRole() {
			return idRole;
		}
		public void setIdRole(Long idRole) {
			this.idRole = idRole;
		}
	}
	
//	@PutMapping("/update")
//	public ResponseEntity<User> UpdateUsers(@RequestBody User user) {
//		return ResponseEntity.ok().body(userService.saveUser(user));
//	}

}
