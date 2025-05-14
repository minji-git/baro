package com.example.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.builder()
			.username("user")
			.password(passwordEncoder().encode("password"))
			.roles("USER")
			.build();
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authz) -> authz
				.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
				.requestMatchers("/signup", "/login").permitAll()
				.anyRequest().authenticated()
			)
			.csrf().disable()
			.headers().frameOptions().disable()
			;

		return http.build();
	}
}
