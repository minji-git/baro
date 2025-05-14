package com.example.task.service;

import com.example.task.dto.SignupRequestDto;
import com.example.task.dto.SignupResponseDto;

import jakarta.validation.Valid;

public interface UserService {
	SignupResponseDto signup(@Valid SignupRequestDto signupRequestDto);
}
