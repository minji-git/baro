package com.example.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "공통 에러 응답 DTO")
@Getter
@Builder
public class ErrorResponseDto {
	private ErrorDetail error;

	@Getter
	@Builder
	public static class ErrorDetail {
		@Schema(description = "에러 코드", example = "USER_ALREADY_EXISTS")
		private String code;
		@Schema(description = "에러 메시지", example = "이미 가입된 사용자입니다.")
		private String message;
	}

	public static ErrorResponseDto createErrorResponse(
		String code, String message) {
		return ErrorResponseDto.builder()
			.error(ErrorDetail.builder()
				.code(code)
				.message(message)
				.build())
			.build();
	}
}
