package com.example.task.service;

import com.example.task.dto.LoginRequestDto;
import com.example.task.dto.LoginResponseDto;
import com.example.task.dto.SignupRequestDto;
import com.example.task.dto.UserResponseDto;

import jakarta.validation.Valid;

public interface UserService {
	UserResponseDto signup(@Valid SignupRequestDto signupRequestDto);
	LoginResponseDto login(@Valid LoginRequestDto loginRequestDto);
}
