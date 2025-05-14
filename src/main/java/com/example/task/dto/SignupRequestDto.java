package com.example.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDto {
	@NotBlank(message = "username은 필수 입력 값입니다.")
	private String username;

	@NotBlank(message = "password는 필수 입력 값입니다.")
	@Size(min = 8, message = "password는 최소 8자 이상이어야 합니다.")
	private String password;

	@NotBlank(message = "nickname은 필수 입력 값입니다.")
	private String nickname;
}
