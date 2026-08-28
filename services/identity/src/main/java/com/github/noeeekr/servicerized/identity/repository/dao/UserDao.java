package com.github.noeeekr.servicerized.identity.repository.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.UserFailure;
import com.github.noeeekr.servicerized.identity.repository.models.User;

public class UserDao {
    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Response<User> getByEmail(String email) {
        try (Session session = this.sessionFactory.openSession()) {
            User u = session
                    .createQuery("FROM User u WHERE u.email = :email AND u.deleted_at = null",
                            User.class)
                    .setParameter("user_email", email).uniqueResult();
            return Response.<User>newInstance().success(u).build();
        } catch (Exception e) {
            return Response.<User>newInstance().fail(new UserFailure.UnhandledException(e)).build();
        }
    }

    public Response<User> saveOne(User u) {
        Transaction tx = null;
        try (Session session = this.sessionFactory.openSession()) {
            tx = session.getTransaction();
            session.persist(u);
            tx.commit();
            return Response.<User>newInstance().success(u).build();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return Response.<User>newInstance().fail(new UserFailure.UnhandledException(e)).build();
        }
    }
}
