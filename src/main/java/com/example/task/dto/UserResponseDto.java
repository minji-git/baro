package com.example.task.dto;

import java.util.List;

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
}
