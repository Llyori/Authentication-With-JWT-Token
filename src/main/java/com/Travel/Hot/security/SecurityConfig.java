package com.Travel.Hot.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Travel.Hot.Filter.CustomAuthenticationFilter;
import com.Travel.Hot.Filter.CustomAuthorizationFilter;
import com.Travel.Hot.security.OAuth2.CustomOAuth2User;
import com.Travel.Hot.security.OAuth2.CustomOauth2UserService;

import lombok.RequiredArgsConstructor;

 
@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private CustomOauth2UserService customOauth2UserService;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
		customAuthenticationFilter.setFilterProcessesUrl("/login");
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.authorizeRequests().antMatchers("/login", "/api/token/refresh").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll()
								.antMatchers("/oauth2/**").permitAll()
								.antMatchers("/api/**").authenticated()
								.antMatchers("/hello").authenticated()
								.antMatchers("/login").authenticated()
								.antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority("ROLE_USER")
								.antMatchers(HttpMethod.POST, "/api/user/newUser/**").hasAnyAuthority("ROLE_ADMIN")
								.anyRequest().permitAll()
								.and()
								.formLogin()
									.loginPage("/index")
									.usernameParameter("username")
									.permitAll()
								.and()
								.oauth2Login()
									.loginPage("/index")
									.userInfoEndpoint()
									.userService(customOauth2UserService)
									.and()
									.successHandler((AuthenticationSuccessHandler) new AuthenticationSuccessHandler() {
										 
								        @Override
								        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
								                Authentication authentication) throws IOException, ServletException {
								 
								            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
								 
								            System.out.println(oauthUser.getName());
								 
								            response.sendRedirect("/hello");
								        }
								    })
								.and();
		http.addFilter(customAuthenticationFilter);
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
}
