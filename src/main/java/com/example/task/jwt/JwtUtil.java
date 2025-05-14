package com.example.task.jwt;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

		Claims claims = Jwts.claims().setSubject(username);
		claims.put("role", role);
		claims.setIssuedAt(now);
		claims.setExpiration(expiryDate);

		return Jwts.builder()
			.setClaims(claims)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// username 추출
	public String getUsernameFromToken(String token) {
		return parseToken(token).getSubject();
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

	// 토큰에서 권한 정보 추출
	public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
		String role = getRoleFromToken(token);
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}
}
