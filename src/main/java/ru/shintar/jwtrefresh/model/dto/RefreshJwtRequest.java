package ru.shintar.jwtrefresh.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Refresh token request")
@AllArgsConstructor
public class RefreshJwtRequest {

    @Schema(description = "Refresh token", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZ...")
    @NotBlank(message = "Refresh token must not be empty")
    @JsonProperty(value = "refreshToken", required = true)
    public String refreshToken;

}