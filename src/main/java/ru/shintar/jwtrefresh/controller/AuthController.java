package ru.shintar.jwtrefresh.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shintar.jwtrefresh.model.dto.AuthRequest;
import ru.shintar.jwtrefresh.model.dto.JwtResponse;
import ru.shintar.jwtrefresh.model.dto.RefreshJwtRequest;
import ru.shintar.jwtrefresh.service.AuthService;

@Tag(name = "Token")
@RestController
@RequestMapping(path = "/api/v1/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;

    @Operation(summary = "Register user")
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody AuthRequest request) {
        final JwtResponse token = authService.register(request);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthRequest request) {
        final JwtResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
