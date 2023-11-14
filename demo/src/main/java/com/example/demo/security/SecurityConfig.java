package com.example.demo.security;


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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


@Bean
public static PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}


@Bean
      SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/products/newProduct").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
       ).httpBasic(Customizer.withDefaults())
            .formLogin((form) -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/products", true)
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
