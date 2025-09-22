package org.example.unisystem.security;

import lombok.extern.slf4j.Slf4j;
import org.example.unisystem.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableWebSecurity
public class SecurityConfig {

    private final UserServiceImpl userService;

    public SecurityConfig(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/uni/students/**", "/uni/submissions/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/uni/courses/**", "/uni/assignments/**").hasAnyRole("PROFESSOR", "ADMIN")
                        .requestMatchers("/uni/**").hasRole("ADMIN")
                        .requestMatchers("/ui/students/**", "/ui/submissions/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/ui/courses/**", "/ui/assignments/**").hasAnyRole("PROFESSOR", "ADMIN")
                        .requestMatchers("/ui/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/ui/students", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

}
