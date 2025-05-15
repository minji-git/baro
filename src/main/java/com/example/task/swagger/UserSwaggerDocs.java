package com.example.task.swagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.task.dto.ErrorResponseDto;
import com.example.task.dto.LoginResponseDto;
import com.example.task.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.METHOD}) // 메서드에만 적용할 수 있음
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지됨
@Documented // javadoc 과 같은 문서에 포함되도록 지정
public @interface UserSwaggerDocs {

	@Target({ElementType.METHOD}) // 메서드에만 적용할 수 있음
	@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지됨
	@Operation(summary = "회원가입", description = "USER 권한으로 회원가입 API")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "회원가입 성공",
			content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
		@ApiResponse(responseCode = "409", description = "이미 가입된 회원 존재",
			content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	})
	@interface PostSignup {}

	@Target({ElementType.METHOD}) // 메서드에만 적용할 수 있음
	@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지됨
	@Operation(summary = "로그인", description = "회원 로그인 API")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 계정 정보(아이디, 비밀번호)",
			content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	})
	@interface PostLogin {}
}
