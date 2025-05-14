package com.example.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.task.entity.Member;

public interface UserRepository extends JpaRepository<Member, String> {
	boolean existsByUsername(String username);

	Optional<Member> findByUsername(String username);

	Optional<Member> findByUserId(Long userId);
}
