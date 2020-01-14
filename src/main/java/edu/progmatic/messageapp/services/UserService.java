package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    Map<String, User> users = new HashMap<>();

    public UserService() {
        createUser(new User("user", "password", "ROLE_USER"));
        createUser(new User("admin", "password", "ROLE_ADMIN"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (users.containsKey(username)) {
            return users.get(username);
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    public void createUser(User user) {
        Assert.isTrue(!userExists(user.getUsername()), "user should not exist");

        users.put(user.getUsername(), user);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public Collection<User> getUsers() {
        return users.values();
    }
}
