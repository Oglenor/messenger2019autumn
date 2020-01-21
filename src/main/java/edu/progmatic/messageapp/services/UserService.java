package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.exceptions.UserAlreadyExistsException;
import edu.progmatic.messageapp.modell.Authority;
import edu.progmatic.messageapp.modell.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = em.createQuery("select u from User u join fetch u.authorities where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return user;
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    @Transactional
    public void createUser(User user) {
        if (userExists(user.getUsername())) {
            throw new UserAlreadyExistsException("User with username: " + user.getUsername() + " already exists!");
        }

        Authority userAuthority = em.createQuery("select a from Authority a where a.name = 'ROLE_USER'", Authority.class).getSingleResult();
        user.addAuthority(userAuthority);
        em.persist(user);
    }

    public boolean userExists(String username) {
        long usersWithUsernameCount = em.createQuery("select count(u) from User u where u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();

        return usersWithUsernameCount > 0;
    }

    public Collection<User> getUsers() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Transactional
    public void updateAuthority(String authorityName) {
        User user = em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", SecurityContextHolder.getContext().getAuthentication().getName())
                .getSingleResult();

        Authority authority = em.createQuery("select a from Authority a where a.name = :name", Authority.class)
                .setParameter("name", authorityName)
                .getSingleResult();

        user.setAuthorities(Collections.singleton(authority));
    }
}
