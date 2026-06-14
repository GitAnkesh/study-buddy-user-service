package com.ankesh.studybuddy.user_service;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class UserServiceApplication {
	@PostConstruct
	public void init() {
		// This forces the entire Spring application JVM to use Kolkata
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	}

	public static void main(String[] args) {
// FORCE the JVM timezone immediately before any framework or driver boots up
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

		// Now it's perfectly safe to launch Spring Boot

		SpringApplication.run(UserServiceApplication.class, args);
	}

}
