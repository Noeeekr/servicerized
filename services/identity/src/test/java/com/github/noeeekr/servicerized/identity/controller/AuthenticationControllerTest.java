package com.github.noeeekr.servicerized.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.AuthenticationService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void testSignUpRoute() throws Exception {
        given(authenticationService.newUser(any()))
                .willReturn(Response.<User>builder().success(new User()).build());

        // Request Content Creation : Create & Validate POST request content
        SignUpRequest request = SignUpRequest.builder().name("TestUser").password("TestUser")
                .email("TestUser@TestDomain.Test").build();
        String content = this.objectMapper.writeValueAsString(request);
        if (content == null) {
            throw new Exception("Unable to transform POST payload into JSON string.");
        }

        // Request Creation : Create Request & Expected Results
        this.mockMvc
                .perform(post("/api/auth/user/signup").content(content)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
