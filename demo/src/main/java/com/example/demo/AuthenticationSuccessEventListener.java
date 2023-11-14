package com.example.demo;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // This method will be called when a user successfully logs in
        String username = event.getAuthentication().getName();
        Collection<? extends GrantedAuthority> authorities = ((User) event.getAuthentication().getPrincipal()).getAuthorities();

        System.out.println("User '" + username + "' with role(s): " + getRoles(authorities) + " has logged in.");
    }

    private String getRoles(Collection<? extends GrantedAuthority> authorities) {
        // Extract and concatenate roles from authorities
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }
    }

