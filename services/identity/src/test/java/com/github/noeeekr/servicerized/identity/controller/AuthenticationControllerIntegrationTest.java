package com.github.noeeekr.servicerized.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noeeekr.servicerized.identity.annotation.InMemoryDatabase;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;

@SpringBootTest
@InMemoryDatabase
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signUpSuccess() throws Exception {
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
