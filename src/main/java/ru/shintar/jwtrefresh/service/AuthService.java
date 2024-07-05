package ru.shintar.jwtrefresh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shintar.jwtrefresh.model.dto.AuthRequest;
import ru.shintar.jwtrefresh.model.dto.JwtResponse;
import ru.shintar.jwtrefresh.model.entity.User;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public JwtResponse register(AuthRequest request) {
        String username = request.getUsername();
        String password = passwordEncoder.encode(request.getPassword());
        User user = userService.createUser(username, password);
        return jwtService.updateRefreshToken(user);
    }

    @Transactional
    public JwtResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }

        User user = userService.loadUserByUsername(request.getUsername());
        return jwtService.updateRefreshToken(user);
    }

    public JwtResponse refresh(String refreshToken) {
        return jwtService.refreshToken(refreshToken);
    }
}