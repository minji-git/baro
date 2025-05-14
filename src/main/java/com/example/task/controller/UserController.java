package com.example.task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.dto.ErrorResponseDto;
import com.example.task.dto.LoginRequestDto;
import com.example.task.dto.LoginResponseDto;
import com.example.task.dto.SignupRequestDto;
import com.example.task.dto.UserResponseDto;
import com.example.task.exception.UserAlreadyExistsException;
import com.example.task.service.UserServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
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

		UserResponseDto responseDto = userService.signup(signupRequestDto);
		log.info("[AuthController] 종료 ## signup 성공");
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(
		@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
		log.info("[UserController] 시작 ## login username={}", loginRequestDto.getUsername());

		LoginResponseDto dto = userService.login(loginRequestDto);

		// 생성된 토큰 HTTP 헤더 담기
		response.addHeader("Authorization", "Bearer " + dto.getToken());
		log.info("[UserController] 종료 ## login 성공 Authorization={}", response.getHeader("Authorization"));
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
}
