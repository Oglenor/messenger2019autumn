package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class TopicService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createTopic(Topic topic) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        topic.setAuthor(currentUser.getUsername());
        em.persist(topic);
    }
}
