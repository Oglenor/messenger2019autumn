package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (userService.userExists(user.getUsername())) {
            bindingResult.rejectValue("username", "username.exists", "Username is already taken!");
            return "register";
        }
        user.addAuthority("ROLE_USER");
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "userList";
    }

    @PostMapping("/user/changeRole")
    public String changeUserRole(@RequestParam String username, @RequestParam String role) {

        User user = (User) userService.loadUserByUsername(username);
        user.setAuthorities(role);

        return "redirect:/users";
    }
}
