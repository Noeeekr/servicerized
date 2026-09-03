package com.github.noeeekr.servicerized.identity.repository.dao;

import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failures;
import com.github.noeeekr.servicerized.identity.common.response.failure.UserFailure;
import com.github.noeeekr.servicerized.identity.repository.dao.configuration.DaoConfiguration;
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
        Transaction tx = null;
        try (Session session = this.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            String query = String.format("UPDATE %s c SET c.%s = true WHERE c.%s = :token",
                    UserEmailConfirmation.class.getName(),
                    UserEmailConfirmation.COLUMN_NAME_CONFIRMED,
                    UserEmailConfirmation.COLUMN_NAME_TOKEN);

            int updatedAmount = session.createMutationQuery(query)
                    .setParameter("token", confirmationToken).executeUpdate();

            if (updatedAmount == 0) {
                Response<?> response = Response.<Boolean>builder().fail(
                        new UserFailure.ResourceNotFound("Código de confirmação não encontrado. "))
                        .build();
                return response;
            }

            tx.commit();
            return Response.<Boolean>builder().success().build();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            return Response.<Boolean>builder().fail(new Failures.UnhandledException(e)).build();
        }
    }

    public Response<UserEmailConfirmation> saveEmailConfirmation(UserEmailConfirmation u,
            DaoConfiguration configuration) {
        Transaction tx = null;
        try (Session session = this.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(u);
            tx.commit();
            return Response.<UserEmailConfirmation>builder().success(u).build();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            return Response.<UserEmailConfirmation>builder()
                    .fail(new Failures.UnhandledException(e)).build();
        }
    }
}
