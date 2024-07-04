package ru.shintar.jwtrefresh.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "User auth request")
public class AuthRequest {

    @Schema(description = "User name", requiredMode = Schema.RequiredMode.REQUIRED, example = "username")
    @Size(min = 3, max = 64, message = "User name must have a length between 3 and 64")
    @NotBlank(message = "User name must not be empty")
    @JsonProperty(value = "username", required = true)
    private String username;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "password")
    @Size(min = 8, max = 64, message = "Password must have a length between 8 and 64")
    @NotBlank(message = "Password must not be empty")
    @JsonProperty(value = "password", required = true)
    private String password;
}
