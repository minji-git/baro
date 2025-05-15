package com.example.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "회원 가입 요청 DTO")
@Getter
@Builder
public class SignupRequestDto {

	@Schema(description = "사용자 아이디", example = "JIN HO")
	@NotBlank(message = "username은 필수 입력 값입니다.")
	private String username;

	@Schema(description = "비밀번호", example = "12341234", minLength = 8)
	@NotBlank(message = "password는 필수 입력 값입니다.")
	@Size(min = 8, message = "password는 최소 8자 이상이어야 합니다.")
	private String password;

	@Schema(description = "닉네임", example = "Mentos")
	@NotBlank(message = "nickname은 필수 입력 값입니다.")
	private String nickname;
}
