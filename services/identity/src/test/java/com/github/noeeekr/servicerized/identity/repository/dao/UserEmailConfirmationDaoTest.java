package com.github.noeeekr.servicerized.identity.repository.dao;

import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.fail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.github.noeeekr.servicerized.identity.annotation.InMemoryDatabase;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.repository.dao.configuration.DaoConfiguration;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;

@SpringBootTest
@InMemoryDatabase
@AutoConfigureMockMvc
class UserEmailConfirmationDaoTest {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    UserEmailConfirmationDao emailConfirmationDao;
    @Autowired
    UserDao userDao;

    @Test
    public void validateUserEmail() {
        UserEmailConfirmation recievedConfirmation = this.createValidateUserEmailDependencies();

        Response<?> validateEmailResponse =
                emailConfirmationDao.validateUserEmail(recievedConfirmation.getToken());
        if (validateEmailResponse.isSuccess() == false) {
            String message = String.format("Falha ao validar o email de usuário: %s",
                    validateEmailResponse.getFailure().message());
            fail(message);
        }

        this.verifyValidateUserEmail(recievedConfirmation);
    }

    // Test Verification Helpers

    public void verifyValidateUserEmail(UserEmailConfirmation recievedConfirmation) {
        this.failOnDifferentVersions(7, recievedConfirmation.getToken().version());

        // Verify if the return token is the expected one
        UserEmailConfirmation expectedConfirmation = null;
        try (Session session = sessionFactory.openSession()) {
            expectedConfirmation = session
                    .createQuery("FROM UserEmailConfirmation c WHERE c.token = :token ",
                            UserEmailConfirmation.class)
                    .setParameter("token", recievedConfirmation.getToken()).uniqueResult();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        this.failOnEmailConfirmationNotFound(recievedConfirmation);
        this.failOnDifferentConfirmations(expectedConfirmation, recievedConfirmation);
        this.failOnNotConfirmed(recievedConfirmation);
    }

    // Test Dependency Helpers

    public UserEmailConfirmation createValidateUserEmailDependencies() {
        // Create target user
        User user = new User().setEmail("test@test.test").setName("test");

        Response<User> createUserResponse = userDao.saveOne(user, new DaoConfiguration());
        if (createUserResponse.isSuccess() == false)
            fail("Failed to create dependency: User entity: Reason: "
                    + createUserResponse.getFailure().message());
        user = createUserResponse.getPayload();

        // Create target email recievedConfirmation
        Response<UserEmailConfirmation> createConfirmationResponse =
                emailConfirmationDao.saveEmailConfirmation(
                        new UserEmailConfirmation().setUser(user), new DaoConfiguration());
        if (createConfirmationResponse.isSuccess() == false)
            fail("Failed to create dependency: User email recievedConfirmation entity: Reason"
                    + createConfirmationResponse.getFailure().message());
        UserEmailConfirmation recievedConfirmation = createConfirmationResponse.getPayload();

        return recievedConfirmation;
    }


    // Fail helpers

    private void failOnEmailConfirmationNotFound(UserEmailConfirmation confirmation) {
        if (confirmation != null)
            return;
        fail("Attempt to find user email confirmation on database failed. ");
    }

    private void failOnDifferentVersions(int expectedVersion, int recievedVersion) {
        if (recievedVersion == expectedVersion)
            return;
        String message = String.format(
                "User Email Confirmation token type is wrong: Expected UUID v7 got UUID v%d",
                recievedVersion);
        fail(message);
    }

    public void failOnDifferentConfirmations(UserEmailConfirmation expected,
            UserEmailConfirmation recieved) {
        if (expected.getToken().equals(recieved.getToken()) == false) {
            String message =
                    String.format("Unexpected token value.\n\tExpected: %s\n\tRecieved: %s",
                            expected.getToken(), recieved.getToken());
            fail(message);
            // Checks if token is confirmed
        }
    }

    public void failOnNotConfirmed(UserEmailConfirmation confirmation) {
        if (confirmation.isConfirmed())
            return;

        System.out.println(confirmation.toString());
        fail("Email Confirmation Token that should be 'confirmed' is not confirmed.");
    }

}
