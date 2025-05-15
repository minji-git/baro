package com.example.task.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
	info = @Info(title = "바로인턴 백엔드 개발 과제(Java)",
		description = "Spring Boot 기반 JWT 인증/인가 로직과 API 구현, Junit 기반 테스트 코드 작성, Swagger 문서화, AWS 배포",
		version = "v1",
		contact = @Contact(name = "과제", url = "(배포 주소)", email = "kmjzz8168@gmail.com")
	))
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(){
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("bearerAuth", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")))
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}
}
