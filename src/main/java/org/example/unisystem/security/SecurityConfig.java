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

                        .requestMatchers(HttpMethod.GET, "/uni/**").permitAll()
                        .requestMatchers("/uni/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/ui/students").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ui/courses").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ui/assignments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ui/professors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ui/submissions").permitAll()

                        .requestMatchers(HttpMethod.GET, "/ui/students/new").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/students/*/edit").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/students/*/patch").authenticated()

                        .requestMatchers(HttpMethod.GET, "/ui/courses/new").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/courses/*/edit").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/courses/*/patch").authenticated()

                        .requestMatchers(HttpMethod.GET, "/ui/assignments/new").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/assignments/*/edit").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/assignments/*/patch").authenticated()

                        .requestMatchers(HttpMethod.GET, "/ui/professors/new").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/professors/*/edit").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/professors/*/patch").authenticated()

                        .requestMatchers(HttpMethod.GET, "/ui/submissions/new").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/submissions/*/edit").authenticated()
                        .requestMatchers(HttpMethod.GET, "/ui/submissions/*/patch").authenticated()

                        .requestMatchers("/ui/**").authenticated()
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
