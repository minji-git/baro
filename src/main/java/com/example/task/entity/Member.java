package com.example.task.entity;

import java.util.List;

import com.example.task.dto.UserResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "members")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(length = 20)
	private String username;

	@Column(length = 100, nullable = false)
	private String password;

	@Column(length = 10, nullable = false, unique = true)
	private String nickname;

	@Enumerated(EnumType.STRING)
	private RoleEum role;

	public static Member createUser(
		String username, String password, String nickname
	) {
		return Member.builder()
			.username(username)
			.password(password)
			.nickname(nickname)
			.role(RoleEum.USER)
			.build();
	}

	public static Member createAdmin(
		String adminUsername, String adminPassword, String adminNickname
	) {
		return Member.builder()
			.username(adminUsername)
			.password(adminPassword)
			.nickname(adminNickname)
			.role(RoleEum.ADMIN)
			.build();
	}

	public UserResponseDto toDto() {
		return UserResponseDto.builder()
			.username(username)
			.nickname(nickname)
			.roles(List.of(UserResponseDto.RoleResponseDto.builder().role(role.getRole()).build()))
			.build();
	}

	public void grantAdminRole() {
		if(!this.role.equals(RoleEum.ADMIN)) {
			this.role = RoleEum.ADMIN;
		}
	}
}
