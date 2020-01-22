package edu.progmatic.messageapp.repositories;

import edu.progmatic.messageapp.modell.Topic;

public interface CustomTopicRepository {
    Topic findByIdWithMessages(long id);
}
