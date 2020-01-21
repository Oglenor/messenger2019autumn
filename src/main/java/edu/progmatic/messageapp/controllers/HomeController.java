package edu.progmatic.messageapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class HomeController {

    private static List<String> greetings = new ArrayList<>();

    private Random random = new Random();

    static{
        greetings.add("#mimindegyéniségekvagyunk");
        greetings.add("#reszkessdzsungelhaindulavadászat");
        greetings.add("#azisteenekafejükrestek");
        greetings.add("#jóreggeltjószurkolást");
    }

    @RequestMapping(value = {"/", "/home"}, method = GET)
    public String home(Model model){
        model.addAttribute("greeting", greetings.get(random.nextInt(greetings.size())) );
        return "home";
    }

}
