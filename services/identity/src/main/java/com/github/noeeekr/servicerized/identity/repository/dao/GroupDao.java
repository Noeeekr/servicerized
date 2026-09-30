package com.github.noeeekr.servicerized.identity.repository.dao;

import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import com.github.noeeekr.servicerized.identity.repository.dao.configuration.DaoConfiguration;
import com.github.noeeekr.servicerized.identity.repository.models.Group;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.user.request.UserSignInInterface;

@Repository
@Transactional
@Scope("prototype")
public class GroupDao {
    @Autowired
    private final SessionFactory sessionFactory;

    public GroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Response<User> getBySigninCredentials(UserSignInInterface request) {
        String query = String.format(
                "FROM User u INNER JOIN u.groups g WHERE g.name = :name AND g.password = :password");
        try (Session session = this.sessionFactory.openSession()) {
            User u = session.createQuery(query, User.class)
                    .setParameter("name", request.getGroupName())
                    .setParameter("password", request.getGroupPassword()).uniqueResult();

            return Response.success(u);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }

    public Response<Group> getOneById(UUID groupId) {
        try (Session session = this.sessionFactory.openSession()) {
            Group g = session.createQuery("FROM Group g WHERE g.id = :id AND g.deletedAt = null",
                    Group.class).setParameter("id", groupId).uniqueResult();
            return Response.success(g);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }

    public Response<Group> getOneByOwnerId(UUID ownerId) {
        try (Session session = this.sessionFactory.openSession()) {
            Group g = session
                    .createQuery("FROM Group g WHERE g.ownerId = :ownerId AND g.deletedAt = null",
                            Group.class)
                    .setParameter("ownerId", ownerId).uniqueResult();
            return Response.success(g);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }

    public Response<Group> saveOne(Group g, DaoConfiguration configuration) {
        Transaction tx = null;
        try (Session session = this.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(g);
            tx.commit();
            return Response.success(g);
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }
}
