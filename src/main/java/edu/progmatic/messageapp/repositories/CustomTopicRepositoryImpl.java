package edu.progmatic.messageapp.repositories;

import edu.progmatic.messageapp.modell.Topic;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CustomTopicRepositoryImpl implements CustomTopicRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Topic findByIdWithMessages(long id) {
        return em.createQuery("select t from Topic t left join fetch where t.id = :id", Topic.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
