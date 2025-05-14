package com.example.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.task.entity.Member;

public interface UserRepository extends JpaRepository<Member, String> {
	boolean existsByUsername(String username);
}
