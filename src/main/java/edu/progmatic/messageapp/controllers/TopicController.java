package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class TopicController {

    private TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/createtopic")
    public String showCreate(Model model) {
        model.addAttribute("topic", new Topic());
        return "createTopic";
    }

    @PostMapping("/createtopic")
    public String createTopic(@Valid @ModelAttribute("topic") Topic topic, BindingResult result) {
        if (result.hasErrors()) {
            return "createTopic";
        }

        topicService.createTopic(topic);
        return "redirect:/messages";
    }
}
