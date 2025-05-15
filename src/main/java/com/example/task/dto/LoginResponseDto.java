package com.example.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "로그인 응답 DTO")
@Getter
@Builder
public class LoginResponseDto {
	@Schema(description = "토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKSU4gSE8iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQ3Mjc1MTgyLCJleHAiOjE3NDcyODIzODJ9.S-bKQVU6VPhM9RfDbEUmSI_w7w8mJb7wdCLUi4HLUfs")
	private String token;
}
