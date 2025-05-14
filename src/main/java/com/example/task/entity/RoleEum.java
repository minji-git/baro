package com.example.task.entity;

import lombok.Getter;

@Getter
public enum RoleEum {
	USER("USER"), ADMIN("Admin");

	private String role;

	RoleEum(String role) {
		this.role = role;
	}
}
