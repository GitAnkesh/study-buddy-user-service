package com.ankesh.studybuddy.user_service.controller;

import com.ankesh.studybuddy.user_service.dto.AuthenticationRequest;
import com.ankesh.studybuddy.user_service.dto.AuthenticationResponse;
import com.ankesh.studybuddy.user_service.dto.RegisterRequest;
import com.ankesh.studybuddy.user_service.dto.UserProfileResponse;
import com.ankesh.studybuddy.user_service.entity.Role;
import com.ankesh.studybuddy.user_service.entity.User;
import com.ankesh.studybuddy.user_service.exception.BadRequestException;
import com.ankesh.studybuddy.user_service.repository.UserRepository;
import com.ankesh.studybuddy.user_service.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address is already registered");
        }

        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER
        );

        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            String jwt = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (AuthenticationException ex) {
            throw new BadRequestException("Invalid email or password");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserProfileResponse profile = new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.ok(profile);
    }
}
