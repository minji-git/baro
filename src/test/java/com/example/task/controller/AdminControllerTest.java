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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.task.dto.LoginRequestDto;
import com.example.task.entity.Member;
import com.example.task.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Long testUserId;
	private String adminToken;
	private String userToken;

	@BeforeEach
	void setUp() throws Exception {
		userRepository.deleteAll();

		// 테스트에 필요한 관리자 계정 미리 저장
		Member adminMember = Member.createAdmin("admin", passwordEncoder.encode("admin1234!"), "관리자");
		userRepository.save(adminMember);

		// 테스트에 필요한 사용자 계정 미리 저장
		Member testMember = Member.createUser("testuser", passwordEncoder.encode("password123"), "테스터");
		userRepository.save(testMember);

		testUserId = testMember.getUserId();
		System.out.println("[AdminControllerTest] setUp() testUserId=" + testUserId);

		// 관리자 계정 로그인 및 토큰 획득
		LoginRequestDto adminLoginDto = LoginRequestDto.builder()
			.username("admin")
			.password("admin1234!")
			.build();

		MvcResult adminLoginResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(adminLoginDto)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		adminToken = objectMapper.readTree(adminLoginResult.getResponse().getContentAsString()).get("token").asText();
		System.out.println("[AdminControllerTest] setUp() adminToken=" + adminToken);
	}

	@DisplayName(value = "관리자 권한 부여 - 성공(ADMIN 권한 계정)")
	@Test
	void grantAdminRole_success() throws Exception {
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/" + testUserId + "/roles")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON));
		resultActions
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.username").value("testuser"))
			.andExpect(jsonPath("$.nickname").value("테스터"))
			.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));

		Member updatedUser = userRepository.findByUserId(testUserId).orElse(null);
		assertNotNull(updatedUser);
		assertEquals("ADMIN", updatedUser.getRole().getRole());
	}

	@DisplayName(value = "USER 권한 회원이 관리자 권한 부여 - 오류(AccessDeniedException 발생)")
	@Test
	void grantAdminRole_for_user_role() throws Exception {
		// 사용자 계정 로그인 및 토큰 획득
		LoginRequestDto userLoginDto = LoginRequestDto.builder()
			.username("testuser")
			.password("password123")
			.build();

		MvcResult userLoginResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userLoginDto)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		userToken = objectMapper.readTree(userLoginResult.getResponse().getContentAsString()).get("token").asText();
		System.out.println("[AdminControllerTest] grantAdminRole_for_user_role() userToken=" + userToken);

		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/" + testUserId + "/roles")
				.header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden())
 			;
//			.andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
//			.andExpect(jsonPath("$.error.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));
	}

	@DisplayName(value = "유효하지 않은 계정에 대한 관리자 권한 부여 - 오류")
	@Test
	void grantAdminRole_invalid_credentials() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/999/roles")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
			.andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
	}
}
