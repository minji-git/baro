package com.example.task.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.task.dto.SignupRequestDto;
import com.example.task.dto.SignupResponseDto;
import com.example.task.entity.Member;
import com.example.task.exception.UserAlreadyExistsException;
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

	@Override
	@Transactional
	public SignupResponseDto signup(@Valid SignupRequestDto signupRequestDto) {
		log.info("[UserServiceImpl] 시작 ## signup ##");

		if(userRepository.existsByUsername(signupRequestDto.getUsername())) {
			throw new UserAlreadyExistsException("이미 가입된 사용자입니다.");
		}

		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
		Member newMember = Member.createUser(
			signupRequestDto.getUsername(), encodedPassword, signupRequestDto.getNickname());

		userRepository.save(newMember);

		return newMember.toDto();
	}
}
