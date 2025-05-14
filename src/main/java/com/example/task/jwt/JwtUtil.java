package com.example.task.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final Key secretKey;

	@Value("${jwt.access-token.expiration}")
	private Long expiration;

	public JwtUtil(@Value("${jwt.secret.key}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// 토큰 생성
	public String generateToken(String username, String role) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);

		return Jwts.builder()
			.setSubject(username)
			.claim("role", role)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// username 추출
	public String getUsernameFromToken(String token) {
		Claims claims = parseToken(token);
		return claims.getSubject();
	}

	// role 추출
	public String getRoleFromToken(String token) {
		Claims claims = parseToken(token);
		return claims.get("role", String.class);
	}

	public boolean validateToken(String token) {
		try{
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Claims parseToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
