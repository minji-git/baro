package com.example.task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.dto.ErrorResponseDto;
import com.example.task.dto.SignupRequestDto;
import com.example.task.dto.SignupResponseDto;
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
		log.info("[UserController] 시작 ## signup request: {}", signupRequestDto);

		try {
			SignupResponseDto responseDto = userService.signup(signupRequestDto);
			log.info("[AuthController] 종료 ## signup 성공");
			return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
		} catch (UserAlreadyExistsException e) {
			ErrorResponseDto errorDto = ErrorResponseDto.builder()
				.error(ErrorResponseDto.ErrorDetail.builder()
					.code("USER_ALREADY_EXISTS")
					.message(e.getMessage())
					.build())
				.build();
			log.warn("[AuthController] 종료 ## signup 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
		}
	}
}
