package com.example.task.jwt;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.task.entity.Member;
import com.example.task.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member target = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("회원 계정(" + username + ")이 존재하지 않습니다."));

		List<SimpleGrantedAuthority> authorities = Collections.singletonList(
			new SimpleGrantedAuthority(target.getRole().getRole()));

		return new User(target.getUsername(), target.getPassword(), authorities);
	}
}