package com.example.task.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.task.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		log.info("[JwtAuthenticationFilter] doFilterInternal 시작 ##");
		String token = resolveToken(request);
		log.info("[JwtAuthenticationFilter] doFilterInternal token={}", token);

		try {
			if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
				String username = jwtUtil.getUsernameFromToken(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
					jwtUtil.getAuthoritiesFromToken(token));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.info("SecurityContext에 인증 정보 설정 - 사용자: {}", username);

				// ADMIN 권한 부여 API 에서, USER는 접근 제한
				if (request.getServletPath().startsWith("/admin/users")
					&& request.getMethod().equals("PATCH")) {
					log.info(">> authentication.getAuthorities()={}", authentication.getAuthorities());
					boolean isAdmin = authentication.getAuthorities().stream()
						.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
					if (!isAdmin) {
						log.error("USER 권한으로 관리자 API 접근 제한: username={}", username);
						sendErrorResponse(response, HttpStatus.FORBIDDEN, "ACCESS_DENIED", "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.");
						return; // 권한 없을 시 필터 체인 중단 및 에러 응답 전송
//						throw new AccessDeniedException("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.");
					}
				}
			} else if (StringUtils.hasText(token)) {
				log.error("유효하지 않은 인증 토큰입니다.");
				sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.");
				return; // 유효하지 않은 토큰 시 필터 체인 중단
				//throw new InvalidTokenException("유효하지 않은 인증 토큰입니다.");
			}
		} catch (ExpiredJwtException e) {
			log.error("JWT 토큰이 만료되었습니다.", e);
			sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.");
			return; // 만료된 토큰 시 필터 체인 중단
		} catch (JwtException e) {
			log.error("JWT 토큰 검증 중 오류가 발생했습니다.", e);
			sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.");
			return; // JWT 관련 예외 발생 시 필터 체인 중단
		}

		log.info("[JwtAuthenticationFilter] doFilterInternal 종료 ##");
		filterChain.doFilter(request, response);
	}

	private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String code, String message) throws IOException {
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		ErrorResponseDto errorResponse = ErrorResponseDto.createErrorResponse(code, message);
		String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

		response.getWriter().write(jsonErrorResponse);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// 특정 경로에 대해서는 토큰 검증 필터링 제외
		return antPathMatcher.match("/signup", request.getServletPath()) ||
			antPathMatcher.match("/login", request.getServletPath()) ||
			antPathMatcher.match("/h2-console/**", request.getServletPath());
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
