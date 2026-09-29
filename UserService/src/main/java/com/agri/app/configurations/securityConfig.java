package com.agri.app.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class securityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.web.SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF MUST be disabled to test POST/PUT requests in Postman!
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(org.springframework.security.config.Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        org.springframework.security.core.userdetails.UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER")
                .build();
        return new org.springframework.security.provisioning.InMemoryUserDetailsManager(user);
    }
}
