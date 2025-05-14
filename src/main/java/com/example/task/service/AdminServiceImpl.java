package com.example.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.task.dto.UserResponseDto;
import com.example.task.entity.Member;
import com.example.task.exception.InvalidCredentialsException;
import com.example.task.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserResponseDto grantAdminRole(Long userId) {
		log.info("[AdminServiceImpl] 시작 ## grantAdminRole");

		// 1. 회원 계정 유효성 검증
		Member targetMember = userRepository.findByUserId(userId)
//			.orElseThrow(() -> new UserNotFoundException("해당 회원 계정을 찾을 수 없습니다."));
			.orElseThrow(() -> new InvalidCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다."));

		// 2. 권한 부여
		targetMember.grantAdminRole();
		userRepository.save(targetMember);

		UserResponseDto responseDto = UserResponseDto.fromEntity(targetMember);
		log.info("[AdminServiceImpl] 종료 ## grantAdminRole");
		return responseDto;
	}
}
