package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.dto.MessageDto;
import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.services.MessageService;
import edu.progmatic.messageapp.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MessageController {

    private MessageService messageService;
    private TopicService topicService;

    @Autowired
    public MessageController(MessageService messageService, TopicService topicService) {
        this.messageService = messageService;
        this.topicService = topicService;
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String showMessages(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to,
            @RequestParam(name = "limit", defaultValue = "100", required = false) Integer limit,
            @RequestParam(name = "orderby", defaultValue = "", required = false) String orderBy,
            @RequestParam(name = "order", defaultValue = "asc", required = false) String order,
            @RequestParam(name = "showDeleted", defaultValue = "false", required = false) boolean showDeleted,
            Model model){

        List<Message> msgs = messageService.filterMessages(id, author, text, from, to, limit, orderBy, order, showDeleted);

        model.addAttribute("msgList", msgs);
        return "messageList";
    }

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") Long msgId,
            Model model){

        Message message = messageService.getMessage(msgId);

        model.addAttribute("message", message);
        return "oneMessage";
    }

    @GetMapping(path = "/showcreate")
    public String showCreateMessage(Model model) {
        MessageDto m = new MessageDto();
        model.addAttribute("message", m);

        model.addAttribute("topics", topicService.getAllTopics());

        return "createMessage";
    }

    @PostMapping(path = "/createmessage")
    public String createMessage(@Valid @ModelAttribute("message") MessageDto m, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("topics", topicService.getAllTopics());
            return "createMessage";
        }

        Message newMessage = messageService.createMessage(m);

        return "redirect:/message/" + newMessage.getId();
    }

    @PostMapping("/messages/delete/{messageId}")
    public String delete(@PathVariable long messageId) {
        messageService.deleteMessage(messageId);
        return "redirect:/messages";
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/messages/delete/{messageId}")
    public @ResponseBody Boolean restDelete(@PathVariable long messageId) {
        return messageService.deleteMessage(messageId);
    }


    @RequestMapping(value = "/messages/{messageId}/newcomment", method = RequestMethod.POST)
    public String newComment(@PathVariable("messageId") long messageId, @Valid @ModelAttribute("comment") Message comment, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            Message message = messageService.getMessage(messageId);
            model.addAttribute("message", message);
            return "oneMessage";
        }

        messageService.addNewComment(messageId, comment);

        return "redirect:/message/" + messageId;
    }
}
