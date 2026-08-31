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
import com.github.noeeekr.servicerized.identity.repository.dao.configuration.DaoConfiguration;
import com.github.noeeekr.servicerized.identity.repository.models.Group;

@Repository
@Scope("prototype")
public class GroupDao {
    @Autowired
    private final SessionFactory sessionFactory;

    public GroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Response<Group> getOneById(UUID groupId) {
        try (Session session = this.sessionFactory.openSession()) {
            Group g = session.createQuery("FROM Group g WHERE g.id = :id AND g.deletedAt = null",
                    Group.class).setParameter("id", groupId).uniqueResult();
            return Response.<Group>builder().success(g).build();
        } catch (Exception e) {
            return Response.<Group>builder().fail(new Failures.UnhandledException(e)).build();
        }
    }

    public Response<Group> getOneByOwnerId(UUID ownerId) {
        try (Session session = this.sessionFactory.openSession()) {
            Group g = session
                    .createQuery("FROM Group g WHERE g.ownerId = :ownerId AND g.deletedAt = null",
                            Group.class)
                    .setParameter("ownerId", ownerId).uniqueResult();
            return Response.<Group>builder().success(g).build();
        } catch (Exception e) {
            return Response.<Group>builder().fail(new Failures.UnhandledException(e)).build();
        }
    }

    public Response<Group> saveOne(Group g, DaoConfiguration configuration) {
        Transaction tx = null;
        try (Session session = this.sessionFactory.openSession()) {
            tx = configuration.getTransaction(() -> {
                return session.beginTransaction();
            });
            session.persist(g);
            tx.commit();
            return Response.<Group>builder().success(g).build();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            return Response.<Group>builder().fail(new Failures.UnhandledException(e)).build();
        }
    }
}
