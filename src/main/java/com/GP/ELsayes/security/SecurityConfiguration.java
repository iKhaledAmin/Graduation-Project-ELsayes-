package com.GP.ELsayes.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authRequest -> {

            authRequest.requestMatchers("/auth/login").permitAll();
            authRequest.requestMatchers("/customers/register").permitAll();


            authRequest.requestMatchers("/customers/edit-profile/{customerId}").hasRole("CUSTOMER");
            authRequest.requestMatchers("/customers/add-car").hasRole("CUSTOMER");


            authRequest.requestMatchers("/customers/get-all-cleaning-services").hasRole("CUSTOMER");
            authRequest.requestMatchers("/customers/get-all-maintenance-services").hasRole("CUSTOMER");
            authRequest.requestMatchers("/customers/get-all-take-away-services").hasRole("CUSTOMER");




        });

        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(AbstractHttpConfigurer::disable);
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
