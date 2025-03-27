package com.microcompany.accountsservice.config;

import com.microcompany.accountsservice.model.ERole;
import com.microcompany.accountsservice.model.User;
import com.microcompany.accountsservice.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class UserDetailsConfig {

    @Autowired
    private UserRepository userRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

            List<User> users = List.of(
                    new User(1, "Manuel@mail.com", enc.encode("my_pass"), ERole.CASHIER),
                    new User(2, "Pepo@mail.com", enc.encode("my_pass"), ERole.DIRECTOR),
                    new User(3, "Laura@mail.com", enc.encode("my_pass"), ERole.DIRECTOR),
                    new User(4, "Sabrina@mail.com", enc.encode("my_pass"), ERole.CASHIER),
                    new User(5, "Jose@mail.com", enc.encode("my_pass"), ERole.DIRECTOR),
                    new User(6, "Roberto@mail.com", enc.encode("my_pass"), ERole.CASHIER),
                    new User(7, "Yolanda@mail.com", enc.encode("my_pass"), ERole.CASHIER),
                    new User(8, "Ramon@mail.com", enc.encode("my_pass"), ERole.CASHIER)
            );

            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return users.stream()
                        .filter(u -> u.getEmail().equals(email))
                        .findFirst()
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario con email: " + email + " no encontrado"));
            }

        };
    }

}
