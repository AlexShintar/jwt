package ru.shintar.jwtrefresh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.shintar.jwtrefresh.model.dto.AuthRequest;
import ru.shintar.jwtrefresh.model.dto.JwtResponse;
import ru.shintar.jwtrefresh.service.AuthService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private ObjectMapper mapper;
    static final String API_PATH = "/api/v1/auth/";

    @Test
    void testRegisterThenTestLogin() throws Exception {
        AuthRequest request = new AuthRequest("Test", "password");
        String json = mapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post(API_PATH + "login")
                        .content(json)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is(403));
        mockMvc.perform(MockMvcRequestBuilders.post(API_PATH + "register")
                        .content(json)
                        .contentType("application/json"))
                .andDo(print()).andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post(API_PATH + "login")
                        .content(json)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testRefresh() throws Exception {
        JwtResponse jwtResponse1 = authService.register(new AuthRequest("test1", "12345678"));
        JwtResponse jwtResponse2 = authService.register(new AuthRequest("test2", "87654321"));

        String token1 = jwtResponse1.getRefreshToken();
        String token2 = jwtResponse2.getRefreshToken();

        String json1 = mapper.writeValueAsString(token1);
        String json2 = mapper.writeValueAsString(token2);

        mockMvc.perform(MockMvcRequestBuilders.post(API_PATH + "refresh")
                        .content(json1)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post(API_PATH + "refresh")
                        .content(json2)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
