package com.example.task.dto;

import java.util.List;

import com.example.task.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "회원가입/관리자 권한 부여 시, 회원 정보 응답 DTO")
@Getter
@Builder
public class UserResponseDto {
	@Schema(description = "사용자 아이디", example = "JIN HO")
	private String username;
	@Schema(description = "닉네임", example = "Mentos")
	private String nickname;
	@Schema(description = "권한", example = "USER or ADMIN")
	private List<RoleResponseDto> roles;

	@Getter
	@Builder
	public static class RoleResponseDto {
		private String role;
	}

	public static UserResponseDto fromEntity(Member member) {
		return UserResponseDto.builder()
			.username(member.getUsername())
			.nickname(member.getNickname())
			.roles(List.of(RoleResponseDto.builder()
				.role(member.getRole().getRole())
				.build()))
			.build();
	}
}
