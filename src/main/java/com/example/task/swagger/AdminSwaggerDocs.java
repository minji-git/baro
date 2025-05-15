package com.example.task.swagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.task.dto.ErrorResponseDto;
import com.example.task.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.METHOD}) // 메서드에만 적용할 수 있음
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지됨
@Documented // javadoc 과 같은 문서에 포함되도록 지정
public @interface AdminSwaggerDocs {

	@Target({ElementType.METHOD}) // 메서드에만 적용할 수 있음
	@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지됨
	@Operation(summary = "관리자 권한 부여", description = "ADMIN 권한 회원이 USER 권한 계정에 관리자 권한을 부여하는 API")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "관리자 권한 부여 성공",
			content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰",
			content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음(관리자 권한 필요한 요청)",
			content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
	})
	@interface PatchGrantAdminRole {}
}
