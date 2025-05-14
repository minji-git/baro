package com.example.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.task.entity.Member;
import com.example.task.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.username}")
	private String adminUsername;

	@Value("${admin.password}")
	private String adminPassword;

	@Value("${admin.nickname}")
	private String adminNickname;

	@PostConstruct
	@Transactional
	public void initAdmin() {
		if(!userRepository.existsByUsername(adminUsername)) {
			log.info("ADMIN 계정 초기화 시작: {}", adminUsername);

			String encodedPassword = passwordEncoder.encode(adminPassword);
			Member admin = Member.createAdmin(
				adminUsername, encodedPassword, adminNickname);

			userRepository.save(admin);

			log.info("ADMIN 계정 초기화 종료");
		}
	}
}
