package com.github.noeeekr.servicerized.identity.repository.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failures;
import com.github.noeeekr.servicerized.identity.repository.dao.configuration.DaoConfiguration;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;

@Repository
@Transactional
@Scope("prototype")
public class UserDao {
    @Autowired
    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Response<List<UserEmailConfirmation>> getEmailConfirmations(String email) {
        try (Session session = this.sessionFactory.openSession()) {
            List<UserEmailConfirmation> confirmations =
                    session.createQuery("FROM UserEmailConfirmation c WHERE c.user.email = :email",
                            UserEmailConfirmation.class).setParameter("email", email).list();
            return Response.<List<UserEmailConfirmation>>builder().success(confirmations).build();
        } catch (Exception e) {
            return Response.<List<UserEmailConfirmation>>builder()
                    .fail(new Failures.UnhandledException(e)).build();
        }
    }

    public Response<User> getByEmail(String email) {
        try (Session session = this.sessionFactory.openSession()) {
            User u = session
                    .createQuery("FROM User u WHERE u.email = :email AND u.deletedAt = null",
                            User.class)
                    .setParameter("email", email).uniqueResult();
            return Response.<User>builder().success(u).build();
        } catch (Exception e) {
            return Response.<User>builder().fail(new Failures.UnhandledException(e)).build();
        }
    }

    public Response<User> saveOne(User u, DaoConfiguration configuration) {
        Transaction tx = null;
        try (Session session = this.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(u);
            tx.commit();
            return Response.<User>builder().success(u).build();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            return Response.<User>builder().fail(new Failures.UnhandledException(e)).build();
        }
    }
}
