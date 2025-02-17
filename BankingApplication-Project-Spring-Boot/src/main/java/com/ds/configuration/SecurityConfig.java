package com.ds.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.ListeningSecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

	@Autowired
	private final UserDetailsService userDetailsService;
	@Autowired
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration ) throws Exception {
		
			return configuration.getAuthenticationManager();
	}
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception { 
		httpSecurity.csrf(csrf -> csrf.disable()) 
		.authorizeHttpRequests(authorize ->authorize 
				.requestMatchers(HttpMethod.POST,"/api-user/user").permitAll()	
		.requestMatchers(HttpMethod.POST,"/api-user/login").permitAll()
						.requestMatchers(HttpMethod.POST,"/api-user/debitAccount").authenticated()
						.requestMatchers(HttpMethod.POST,"/api-user/creditAccount").authenticated()
						.requestMatchers(HttpMethod.GET,"api-user/balanceEnquiry").authenticated()
						.requestMatchers(HttpMethod.POST,"/api-user/transfer").authenticated()
						.requestMatchers(HttpMethod.GET,"/api-user/statement").authenticated()
		.anyRequest().authenticated()); 
		
		httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.authenticationProvider(authenticationProvider()); 
		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); 
		return httpSecurity.build();
	}

}
	
	

