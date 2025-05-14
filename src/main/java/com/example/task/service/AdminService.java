package com.example.task.service;

import com.example.task.dto.UserResponseDto;

public interface AdminService {
	UserResponseDto grantAdminRole(Long userId);
}
