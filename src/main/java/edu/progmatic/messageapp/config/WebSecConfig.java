package edu.progmatic.messageapp.config;

import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        manager.createUser(new User("user", "password", "ROLE_USER"));
//        manager.createUser(new User("admin", "password", "ROLE_ADMIN"));
//        return manager;
//    }

    @SuppressWarnings("deprecation")
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                //.loginPage("/login").permitAll()
                //.loginProcessingUrl("/login")
                .defaultSuccessUrl("/messages", true)
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .and()
                .authorizeRequests()
                .antMatchers("/home", "/register").permitAll()
                .antMatchers("/webjars/bootstrap/**", "/webjars/jquery/**", "/webjars/popper.js/**").permitAll()
                .antMatchers("/statistics").hasRole("ADMIN")
                .antMatchers("/users", "/user/changeRole").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

}
