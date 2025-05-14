package com.example.task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.dto.ErrorResponseDto;
import com.example.task.dto.LoginRequestDto;
import com.example.task.dto.LoginResponseDto;
import com.example.task.dto.SignupRequestDto;
import com.example.task.dto.UserResponseDto;
import com.example.task.exception.InvalidCredentialsException;
import com.example.task.exception.UserAlreadyExistsException;
import com.example.task.service.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl userService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		log.info("[UserController] 시작 ## signup request={}", signupRequestDto);

		try {
			UserResponseDto responseDto = userService.signup(signupRequestDto);
			log.info("[AuthController] 종료 ## signup 성공");
			return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
		} catch (UserAlreadyExistsException e) {
			ErrorResponseDto errorDto = ErrorResponseDto.builder()
				.error(ErrorResponseDto.ErrorDetail.builder()
					.code("USER_ALREADY_EXISTS")
					.message(e.getMessage())
					.build())
				.build();
			log.error("[AuthController] 종료 ## signup 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		log.info("[UserController] 시작 ## login username={}", loginRequestDto.getUsername());

		try {
			LoginResponseDto dto = userService.login(loginRequestDto);
			log.info("[AuthController] 종료 ## login 성공");
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} catch (InvalidCredentialsException e) {
			ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_CREDENTIALS", e.getMessage());
			log.error("[AuthController] 종료 ## login 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
		}
	}

	@PatchMapping("/admin/users/{userId}/roles")
	public ResponseEntity<?> grantAdmin(@Valid @PathVariable(value = "userId") Long userId) {
		log.info("[UserController] 시작 ## getAdmin userId={}", userId);

		try {
			UserResponseDto dto = userService.grantAdmin(userId);
			log.info("[AuthController] 종료 ## grantAdmin 성공");
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} catch (AccessDeniedException e) {
			ErrorResponseDto errorDto = ErrorResponseDto.createErrorResponse("INVALID_CREDENTIALS", e.getMessage());
			log.error("[AuthController] 종료 ## grantAdmin 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
		}
	}
}
