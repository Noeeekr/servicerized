package com.github.noeeekr.servicerized.identity.repository.dao;

import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import com.github.noeeekr.servicerized.identity.response.failure.UserFailure;
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
                tx.rollback();
                Response<?> response = Response.fromFailure(
                        new UserFailure.ResourceNotFound("Código de confirmação não encontrado. "));
                return response;
            }

            tx.commit();
            return Response.success(null);
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }

    public Response<UserEmailConfirmation> saveEmailConfirmation(UserEmailConfirmation u,
            DaoConfiguration configuration) {
        Transaction tx = null;
        try (Session session = this.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(u);
            tx.commit();
            return Response.success(u);
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }
}
