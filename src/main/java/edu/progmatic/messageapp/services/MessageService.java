package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @PersistenceContext
    EntityManager em;


    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

//    private List<Message> messages = new ArrayList<>();
//
//    {
//        messages.add(new Message("Aladár", "Mz/x jelkezz, jelkezz", LocalDateTime.now().minusDays(10)));
//        messages.add(new Message("Kriszta", "Bemutatom lüke Aladárt", LocalDateTime.now().minusDays(5)));
//        messages.add(new Message("Blöki", "Vauuu", LocalDateTime.now()));
//        messages.add(new Message("Maffia", "miauuu", LocalDateTime.now()));
//        messages.add(new Message("Aladár", "Kapcs/ford", LocalDateTime.now().plusDays(5)));
//        messages.add(new Message("Aladár", "Adj pénzt!", LocalDateTime.now().plusDays(10)));
//    }

    @PostFilter("(hasRole('ADMIN') and #showDeleted == true) or filterObject.deleted == false")
    public List<Message> filterMessages(Long id, String author, String text, LocalDateTime from, LocalDateTime to, Integer limit, String orderBy, String order, boolean showDeleted) {
        LOGGER.info("filterMessages method started");
        LOGGER.debug("id: {}, author: {}, text: {}", id, author, text);

        List<Message> messages = em.createQuery("SELECT m FROM Message m", Message.class).getResultList();
        Comparator<Message> msgComp = Comparator.comparing((Message::getCreationDate));
        LOGGER.debug("filterMessages is going to compare...");
        switch (orderBy) {
            case "text":
                LOGGER.trace("comparing by text");
                msgComp = Comparator.comparing((Message::getText));
                break;
            case "id":
                LOGGER.trace("comparing by id");
                msgComp = Comparator.comparing((Message::getId));
                break;
            case "author":
                LOGGER.trace("comparing by author");
                msgComp = Comparator.comparing((Message::getAuthor));
                break;
            default:
                break;
        }
        if (order.equals("desc")) {
            msgComp = msgComp.reversed();
        }

        List<Message> msgs = messages.stream()
                .filter(m -> id == null ? true : m.getId().equals(id))
                .filter(m -> StringUtils.isEmpty(author) ? true : m.getAuthor().contains(author))
                .filter(m -> StringUtils.isEmpty(text) ? true : m.getText().contains(text))
                .filter(m -> from == null ? true : m.getCreationDate().isAfter(from))
                .filter(m -> to == null ? true : m.getCreationDate().isBefore(to))
                .sorted(msgComp)
                .limit(limit).collect(Collectors.toList());


        return msgs;
    }

    @Transactional
    public Message getMessage(Long msgId) {
        Message message =em.find(Message.class, msgId);

        return message;
    }

    @Transactional
    public void createMessage(Message m, Topic topic) {
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        m.setAuthor(loggedInUserName);
        m.setCreationDate(LocalDateTime.now());
//        m.setId((long) messages.size());

        m.setTopic(topic);
        em.persist(m);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMessage(long messageId) {

        Message message = em.find(Message.class, messageId);
        message.setDeleted(true);
    }

    @Transactional
    public void addNewComment(long parentMessageId, Message comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Message parentMessage = getMessage(parentMessageId);
        comment.setAuthor(user.getUsername());
        comment.setParent(parentMessage);
        comment.setTopic(parentMessage.getTopic());
        comment.setCreationDate(LocalDateTime.now());

        em.persist(comment);
    }
}
