package com.example.household_app.auth;

import com.example.household_app.auth.dto.LoginRequest;
import com.example.household_app.security.JwtTokenProvider;
import com.example.household_app.user.User;
import com.example.household_app.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtTokenProvider.generateToken(
                user.getId(),
                user.getEmail(),
                "ROLE_" + user.getRole().name()
        );

    }
}
