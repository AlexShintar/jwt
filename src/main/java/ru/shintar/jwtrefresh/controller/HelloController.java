package ru.shintar.jwtrefresh.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shintar.jwtrefresh.service.AuthService;

@Tag(name = "Hello")
@RestController
@RequestMapping(path = "/api/v1/hello",
        produces = MediaType.TEXT_PLAIN_VALUE)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class HelloController {

    final private AuthService authService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/user")
    @Operation(
            summary = "Hello user endpoint",
            description = "Returns a hello message for the authorized user",
            responses = @ApiResponse(
                    responseCode = "200"
            )
    )
    public ResponseEntity<String> helloUser() {
        Authentication authentication = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authentication.getPrincipal());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/admin")
    @Operation(
            summary = "Hello admin endpoint",
            description = "Returns a hello message for the authorized admin",
            responses = @ApiResponse(
                    responseCode = "200"
            )
    )
    public ResponseEntity<String> helloAdmin() {
        Authentication authentication = authService.getAuthInfo();
        return ResponseEntity.ok("Hello admin " + authentication.getPrincipal());
    }
}