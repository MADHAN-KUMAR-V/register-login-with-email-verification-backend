package com.madhan65.emailverification.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security)throws Exception{
        return security
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/register/**").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/user/**")
                .hasAnyAuthority("USER","ADMIN")
                .and()
                .formLogin()
                .and()
                .build();
    }

}
