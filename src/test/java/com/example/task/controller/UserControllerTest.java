package com.example.task.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.task.dto.LoginRequestDto;
import com.example.task.dto.SignupRequestDto;
import com.example.task.entity.Member;
import com.example.task.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		// 각 테스트 시작 전에 데이터베이스 초기화 (기존 데이터 삭제)
		userRepository.deleteAll();

		// 테스트에 필요한 사용자 미리 저장
		Member testMember = Member.createUser("testuser", passwordEncoder.encode("password123"), "테스터");
		userRepository.save(testMember);
	}

	@DisplayName(value = "회원가입 - 성공")
	@Test
	void signup_success() throws Exception {
		SignupRequestDto signupRequestDto = SignupRequestDto.builder()
			.username("newuser")
			.password("password123")
			.nickname("별명")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signupRequestDto)))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(jsonPath("$.username").value("newuser"))
			.andExpect(jsonPath("$.nickname").value("별명"))
			.andExpect(jsonPath("$.roles[0].role").value("USER"));

		// 데이터베이스에 저장되었는지 확인
		assertTrue(userRepository.existsByUsername("newuser"));

		Member savedMember = userRepository.findByUsername("newuser").orElse(null);
		assertTrue(passwordEncoder.matches("password123", savedMember.getPassword()));
	}

	@DisplayName(value = "회원가입 - 이미 가입한 계정 존재")
	@Test
	void signup_user_already_exists() throws Exception {
		// 먼저 정상 회원가입 수행
		signup_success();

		SignupRequestDto signupRequestDto = SignupRequestDto.builder()
			.username("newuser")
			.password("anotherpassword")
			.nickname("새로운별명")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signupRequestDto)))
			.andExpect(MockMvcResultMatchers.status().isConflict())
			.andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"))
			.andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."));
	}

	@DisplayName(value = "로그인 - 성공")
	@Test
	void login_success() throws Exception {
		LoginRequestDto loginRequestDto = LoginRequestDto.builder()
			.username("testuser")
			.password("password123")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequestDto)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.token").isString())
			.andExpect(MockMvcResultMatchers.header().exists("Authorization"));
	}

	@DisplayName(value = "로그인 - 잘못된 자격증명")
	@Test
	void login_invalid_credentials() throws Exception {
		LoginRequestDto loginRequestDto = LoginRequestDto.builder()
			.username("nonexistentuser")
			.password("password123")
			.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequestDto)))
			.andExpect(MockMvcResultMatchers.status().isUnauthorized())
			.andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
			.andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
	}

}