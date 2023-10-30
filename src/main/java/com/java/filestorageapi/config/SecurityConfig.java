package com.java.filestorageapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable())
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                                .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v2/api-docs"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/webjars/**"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/authenticate/**"))
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();



    }
}
