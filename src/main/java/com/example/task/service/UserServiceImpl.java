package com.example.task.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.task.dto.LoginRequestDto;
import com.example.task.dto.LoginResponseDto;
import com.example.task.dto.SignupRequestDto;
import com.example.task.dto.UserResponseDto;
import com.example.task.entity.Member;
import com.example.task.exception.InvalidCredentialsException;
import com.example.task.exception.UserAlreadyExistsException;
import com.example.task.exception.UserNotFoundException;
import com.example.task.jwt.JwtUtil;
import com.example.task.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	@Transactional
	public UserResponseDto signup(@Valid SignupRequestDto signupRequestDto) {
		log.info("[UserServiceImpl] 시작 ## signup ##");

		// 1. 계정 존재 확인
		if(userRepository.existsByUsername(signupRequestDto.getUsername())) {
			throw new UserAlreadyExistsException("이미 가입된 사용자입니다.");
		}

		// 2. password 해싱 암호화 및 계정 생성
		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
		Member newMember = Member.createUser(
			signupRequestDto.getUsername(), encodedPassword, signupRequestDto.getNickname());

		userRepository.save(newMember);

		log.info("[UserServiceImpl] 종료 ## signup ##");
		return newMember.toDto();
	}

	@Override
	@Transactional
	public LoginResponseDto login(@Valid LoginRequestDto loginRequestDto) {
		log.info("[UserServiceImpl] 시작 ## login ##");

		// 1. username 유효성 검증
		Member targetMember = userRepository.findByUsername(loginRequestDto.getUsername())
			.orElseThrow(() -> new UserNotFoundException("회원 계정이 존재하지 않습니다."));

		// 2. password 유효성 검증
		log.info(">> loginRequestDto.getPassword(): {}", loginRequestDto.getPassword());
		log.info(">> targetMember.getPassword(): {}", targetMember.getPassword());
		if(!passwordEncoder.matches(loginRequestDto.getPassword(), targetMember.getPassword())) {
			throw new InvalidCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
		}

		// 3. 토큰 생성
		String token = jwtUtil.generateToken(targetMember.getUsername(), targetMember.getRole().getRole());
		boolean validToken = jwtUtil.validateToken(token);
		if(!validToken) {
			throw new InvalidCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
		}

		log.info("[UserServiceImpl] 종료 ## login ## : token={}", token);

		return LoginResponseDto.builder()
			.token(token)
			.build();
	}
}
