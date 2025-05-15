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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.task.dto.LoginRequestDto;
import com.example.task.entity.Member;
import com.example.task.jwt.JwtUtil;
import com.example.task.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
// 테스트 환경에서 JWT 유효 기간을 짧게 설정
@TestPropertySource(properties = "jwt.access-token.expiration=2000") // 2초
public class JwtExpirationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	private String userToken;
	private Long testUserId;

	@BeforeEach
	void setUp() throws Exception {
		userRepository.deleteAll();

		// 테스트에 필요한 관리자 계정 미리 저장
		Member adminMember = Member.createAdmin("admin", passwordEncoder.encode("admin1234!"), "관리자");
		userRepository.save(adminMember);

		// 테스트에 필요한 사용자 계정 미리 저장
		Member testMember = Member.createUser("expireuser", passwordEncoder.encode("password123"), "만료테스트유저");
		userRepository.save(testMember);

		// 사용자 계정 로그인 및 토큰 획득
		LoginRequestDto userLoginDto = LoginRequestDto.builder()
			.username("expireuser")
			.password("password123")
			.build();

		MvcResult userLoginResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userLoginDto)))
			.andExpect(status().isOk())
			.andReturn();
		userToken = objectMapper.readTree(userLoginResult.getResponse().getContentAsString()).get("token").asText();
		System.out.println("[AdminControllerTest] setUp() adminToken=" + userToken);

		Member testUser = userRepository.findByUsername("expireuser").orElse(null);
		assertNotNull(testUser);
		testUserId = testUser.getUserId();
		System.out.println("[JwtExpirationTest] setUp() testUserId=" + testUserId);
	}

	@DisplayName("만료된 토큰으로 API 접근 시 UNAUTHORIZED 응답")
	@Test
	void accessApiWithExpiredToken() throws Exception {
		// 간단하게 Thread.sleep()을 사용하여 토큰 만료 시간 초과
		Thread.sleep(5000); // 토큰 유효 기간(2초)보다 충분히 긴 시간 대기

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/" + testUserId) // 임의의 보호된 API 엔드포인트
				.header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error.code").value("INVALID_TOKEN"))
			.andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."));
	}
}
