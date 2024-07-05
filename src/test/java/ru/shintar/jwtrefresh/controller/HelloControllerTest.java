package ru.shintar.jwtrefresh.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.shintar.jwtrefresh.model.dto.AuthRequest;
import ru.shintar.jwtrefresh.model.dto.JwtResponse;
import ru.shintar.jwtrefresh.service.AuthService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class HelloControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    static final String API_PATH = "/api/v1/hello/";

    @Test
    void userIsNotAuthenticatedAccessIsForbidden() throws Exception {
        mockMvc.perform(get(API_PATH + "user"))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void userAuthenticatedAccess() throws Exception {
        JwtResponse jwtResponse = authService.register(new AuthRequest("Test", "12345678"));
        String token = jwtResponse.getAccessToken();

        mockMvc.perform(get(API_PATH + "user")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
