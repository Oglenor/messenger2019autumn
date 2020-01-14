package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.services.MessageService;
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



    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
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
        Message m = new Message();
        model.addAttribute("message", m);
        return "createMessage";
    }

    @PostMapping(path = "/createmessage")
    public String createMessage(@Valid @ModelAttribute("message") Message m, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createMessage";
        }

        messageService.createMessage(m);
        //return "home";
        //return "redirect:/messages?orderby=createDate&order=desc";
        //return "redirect:/messages?id=" + m.getId();
        return "redirect:/message/" + m.getId();
    }

    @PostMapping("/messages/delete/{messageId}")
    public String delete(@PathVariable long messageId) {
        messageService.deleteMessage(messageId);
        return "redirect:/messages";
    }

}
