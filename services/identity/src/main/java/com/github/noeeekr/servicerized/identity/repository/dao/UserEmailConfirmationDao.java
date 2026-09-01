package com.github.noeeekr.servicerized.identity.repository.dao;

import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failures;
import com.github.noeeekr.servicerized.identity.common.response.failure.UserFailure;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;
import jakarta.transaction.Transactional;

@Repository
@Transactional
@Scope("prototype")
public class UserEmailConfirmationDao {
    @Autowired
    private final SessionFactory sessionFactory;

    public UserEmailConfirmationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Response<?> validateUserEmail(UUID confirmationToken) {
        try (Session session = this.sessionFactory.openSession()) {
            String query = String.format("UPDATE %s c SET c.%s = true WHERE c.%s = :token",
                    UserEmailConfirmation.class.getName(),
                    UserEmailConfirmation.COLUMN_NAME_CONFIRMED,
                    UserEmailConfirmation.COLUMN_NAME_TOKEN);

            int updatedAmount = session.createQuery(query, UserEmailConfirmation.class)
                    .setParameter("token", confirmationToken).executeUpdate();

            if (updatedAmount == 0) {
                Response<?> response = Response.<Boolean>builder().fail(
                        new UserFailure.ResourceNotFound("Código de confirmação não encontrado. "))
                        .build();
                return response;
            }
            
            return Response.<Boolean>builder().success().build();
        } catch (Exception e) {
            return Response.<Boolean>builder().fail(new Failures.UnhandledException(e)).build();
        }

    }
}
