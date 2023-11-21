package com.example.demo.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.oauth2.login.UserInfoEndpointDsl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


@Bean
public static PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}


    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
@Bean
      SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/registration").permitAll()
                    .requestMatchers("/home").permitAll()
                    .requestMatchers("/products/newProduct").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
       ).httpBasic(Customizer.withDefaults())
            .formLogin((form) -> form
                    .loginPage("/login")
//                    .defaultSuccessUrl("/users/profile", true)
                            .successHandler(successHandler)

                            .permitAll()
            )
            .logout((logout) -> logout.permitAll());

    return http.build();
}



//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//
//                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated()
//                ).httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }


}
