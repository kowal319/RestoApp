package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetails implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            new UsernameNotFoundException("User not exist");
        }
            Set<GrantedAuthority> grantedAuthoritySet = user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthoritySet);


    }
}
