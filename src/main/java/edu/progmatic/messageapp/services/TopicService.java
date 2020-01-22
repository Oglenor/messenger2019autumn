package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Transactional
    public void createTopic(Topic topic) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        topic.setAuthor(currentUser.getUsername());
        topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicByIdWithMessages(long topicId) {
        Topic t = topicRepository.findByIdWithMessages(topicId);
        return t;
    }

    public Topic getTopicByTitle(String title) {
        Topic topic = topicRepository.findByTitle(title);

        return topic;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteTopic(long topicId) {
        topicRepository.deleteById(topicId);
    }
}
