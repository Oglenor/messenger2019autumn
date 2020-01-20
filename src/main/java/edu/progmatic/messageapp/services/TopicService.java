package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public List<Topic> getAllTopics() {
        List<Topic> topics =
                em.createQuery("SELECT t FROM Topic t", Topic.class).getResultList();
        return topics;
    }

    public Topic getTopicByIdWithMessages(long topicId) {
        return em.find(Topic.class, topicId);
    }

    public Topic getTopicByTitle(String title) {
        Topic topic =
                em.createQuery("SELECT t FROM Topic t WHERE t.title = :title", Topic.class)
                .setParameter("title", title).getSingleResult();
        return topic;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delteTopic(long topicId) {
        em.remove(em.find(Topic.class, topicId));
    }
}
