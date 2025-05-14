package com.example.task.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
	private String token;
}
