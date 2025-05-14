package com.example.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.task.dto.ErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
		log.warn("중복 가입 실패: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_CREDENTIALS", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponseDto> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		log.warn("인증 실패: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_CREDENTIALS", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
		log.warn("사용자 계정 조회 실패: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("USER_NOT_FOUND", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
		log.warn("접근 권한 제한: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("ACCESS_DENIED", "접근 권한이 없습니다.");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponseDto> handleInvalidTokenException(InvalidTokenException ex) {
		log.warn("유효하지 않은 인증 토큰: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_TOKEN", "접근 권한이 없습니다.");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
	}

	@ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
	public ResponseEntity<ErrorResponseDto> handleExpiredJwtException(io.jsonwebtoken.ExpiredJwtException ex) {
		log.warn("JWT 토큰 만료: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_TOKEN", "만료된 인증 토큰입니다.");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
	}

	@ExceptionHandler(io.jsonwebtoken.security.SignatureException.class)
	public ResponseEntity<ErrorResponseDto> handleSignatureException(io.jsonwebtoken.security.SignatureException ex) {
		log.warn("JWT 서명 오류: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
	}

	@ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
	public ResponseEntity<ErrorResponseDto> handleMalformedJwtException(io.jsonwebtoken.MalformedJwtException ex) {
		log.warn("JWT 형식 오류: {}", ex.getMessage());
		ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
	}
}
