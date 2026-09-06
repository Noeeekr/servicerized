package com.github.noeeekr.servicerized.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"in-memory-db", "local-mailer"})
public class AuthenticationControllerIntegrationTest {
        private String testUserName = "TestUser";
        private String testUserEmail = "TestUser@TestDomain.Test";
        private String testUserPassword = "TestUser";

        private static String testMailerUserName = "servicerized";
        private static String testMailerUserPassword = "servicerized";

        @RegisterExtension
        public static GreenMailExtension greenMail = new GreenMailExtension(
                        new ServerSetup(3050, "127.0.0.1", ServerSetup.PROTOCOL_SMTP))
                                        .withConfiguration(new GreenMailConfiguration().withUser(
                                                        testMailerUserName, testMailerUserPassword))
                                        .withPerMethodLifecycle(true);

        @DynamicPropertySource
        public static void configureMailProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.mail.username", () -> testMailerUserName);
                registry.add("spring.mail.password", () -> testMailerUserPassword);
                registry.add("spring.mail.host", () -> "127.0.0.1");
                registry.add("spring.mail.port", () -> 3050);
        }

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private SessionFactory sessionFactory;

        @Test
        @Order(1000)
        public void signUpSuccess() throws Exception {
                // Test Configuration : Create & Validate POST request content
                SignUpRequest request = SignUpRequest.builder().name(this.testUserName)
                                .password(this.testUserPassword).email(this.testUserEmail).build();
                String content = this.objectMapper.writeValueAsString(request);
                if (content == null) {
                        fail("Test Configuration: Unable to transform POST payload into JSON string.");
                        return;
                }

                // Test Creation : Create Request & Expected Results
                String url = "" + String.format("%s%s", AuthenticationController.CONTROLLER_PATH,
                                AuthenticationController.CONTROLLER_SIGNUP_PATH);
                this.mockMvc.perform(
                                post(url).content(content).accept(MediaType.APPLICATION_JSON_VALUE)
                                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        }


        /**
         * {@link #signUpConfirmationSuccess()} tests if method
         * {@link AuthenticationController#signUpConfirmation()} enters in success state under the
         * the expected successfull request format condition. It also expects method
         * {@link #signUpSuccess()} to be run before it and configure the necessary dependencies for
         * this test.
         * 
         * @throws Exception
         */
        @Test
        @Order(2000)
        public void signUpConfirmationSuccess() throws Exception {
                // Test Configuration : Create & Validate POST request content
                String query = String.format(
                                "SELECT c FROM %s c INNER JOIN %s u ON c.user = u WHERE u.email = :testUserEmail",
                                UserEmailConfirmation.class.getSimpleName(),
                                User.class.getSimpleName());
                String confirmationToken = null;
                try (Session session = sessionFactory.openSession()) {
                        UserEmailConfirmation confirmation =
                                        session.createQuery(query, UserEmailConfirmation.class)
                                                        .setParameter("testUserEmail",
                                                                        this.testUserEmail)
                                                        .getSingleResult();
                        confirmationToken = confirmation.getToken().toString();
                } catch (Exception e) {
                        fail("Test Configuration: Unable to get test user sign-up token. Reason: "
                                        + e.getMessage());
                } finally {
                        if (confirmationToken == null) {
                                fail("Test Configuration: Unable to get test user sign-up token. Reason: Confirmation token not found.");
                        }
                }

                String requestPath = "" + String.format("%s%s?%s=%s",
                                AuthenticationController.CONTROLLER_PATH,
                                AuthenticationController.CONTROLLER_SIGNUP_CONFIRMATION_PATH,
                                AuthenticationController.QUERY_PARAM_EMAIL_CONFIRMATION_TOKEN,
                                confirmationToken);

                // Test Creation : Create Request & Expected Results
                this.mockMvc.perform(get(requestPath).accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))

                                .andExpect(status().is(HttpStatus.OK.value()))
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        }

}
