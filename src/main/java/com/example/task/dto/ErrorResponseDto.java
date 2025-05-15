package com.example.task.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
	private ErrorDetail error;

	@Getter
	@Builder
	public static class ErrorDetail {
		private String code;
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
