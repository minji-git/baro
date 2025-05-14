package com.example.task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.dto.UserResponseDto;
import com.example.task.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminController {

	private final AdminService adminService;

	@PatchMapping("/{userId}/roles")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> grantAdminRole(@Valid @PathVariable(value = "userId") Long userId) {
		log.info("[AdminController] 시작 ## grantAdminRole userId={}", userId);
		UserResponseDto dto = adminService.grantAdminRole(userId);
		log.info("[AdminController] 종료 ## grantAdminRole");
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
}
