package com.example.task.dto;

import java.util.List;

import com.example.task.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
	private String username;
	private String nickname;
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
