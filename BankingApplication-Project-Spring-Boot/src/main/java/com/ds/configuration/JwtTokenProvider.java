package com.ds.configuration;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider  {
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwr.expiration}")
	private long jwtExpiration;
	
	public String generateToken(org.springframework.security.core.Authentication authentication) {
		String username=authentication.getName();
		Date currentDate = new Date();
		Date expairDate=new Date(currentDate.getTime() + jwtExpiration);
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(currentDate)
				.setExpiration(expairDate)
				.signWith(key())
				.compact();
				
	}
	private Key key() {
		byte[] bytes=Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(bytes);
		
	}
	
	public String getUsername(String token) {
		Claims claims=Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key())
			.build()
			.parse(token);
			return true;
		}catch ( ExpiredJwtException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	
	

}
