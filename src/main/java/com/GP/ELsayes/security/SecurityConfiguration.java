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
            authRequest.requestMatchers("/customers/**").hasRole("CUSTOMER");



            authRequest.requestMatchers(
                    "/workers/edit-profile/{workerId}"
                            , "/workers/change-worker-status/{workerId}"
                            ,"/finish-task"
                    ).hasAnyRole("PARKING_WORKER", "CLEANING_WORKER","MAINTENANCE_WORKER");

            authRequest.requestMatchers(
                    "/workers/check-out"
                            , "/workers/generate-free-code/{workerId}"
                            , "/workers/record-visitation"
                    ).hasRole("PARKING_WORKER");




            authRequest.requestMatchers(
                    "/managers/edit-profile/{managerId}",
                    "/managers/add-worker",
                    "/managers/update-worker/{workerId}",
                    "/managers/delete-worker/{workerId}",
                    "/managers/get-worker-by-id/{workerId}",
                    "/managers/get-all-workers-by-branchId/{branchId}",
                    "/managers/get-customer-by-id/{customerId}",
                    "/managers/get-all-services",
                    "/managers/get-service-by-id/{serviceId}",
                    "/managers/add-service-to-branch",
                    "/managers/activate-service-in-branch",
                    "/managers/deactivate-service-in-branch",
                    "/managers/get-all-service-by-branch-id/{branchId}",
                    "/managers/get-package-by-id/{packageId}",
                    "/managers/get-all-packages",
                    "/managers/add-package-to-branch",
                    "/managers/activate-package-in-branch",
                    "/managers/deactivate-package-in-branch",
                    "/managers/get-all-packages-by-branch-id/{branchId}",
                    "/managers/get-manager-by-id/{managerId}"
                    ).hasAnyRole("TOP_MANAGER", "MANAGER");



            authRequest.requestMatchers(
                    "/managers/get-all-workers",
                    "/managers/update-customer/{customerId}",
                    "/managers/delete-customer/{customerId}",
                    "/managers/get-all-customers",
                    "/managers/add-service",
                    "/managers/update-service/{serviceId}",
                    "/managers/delete-service/{serviceId}",
                    "/managers/add-service-to-package",
                    "/managers/add-package",
                    "/managers/update-package/{packageId}",
                    "/managers/delete-package/{packageId}"
                    ).hasRole("TOP_MANAGER");




            authRequest.requestMatchers("/owners/register-owner").permitAll();

            authRequest.requestMatchers(
                    "/owners/edit-profile/{ownerId}",
                    "/owners/add-owner",
                    "/owners/get-all",
                    "/owners/get-by-id/{ownerId}",
                    "/owners/add-manager",
                    "/owners/update-manager/{managerId}",
                    "/owners/delete-manager/{managerId}",
                    "/owners/get-all-managers",
                    "/owners/get-manager-by-id/{managerId}",
                    "/owners/get-manager-by-branchId/{branchId}",
                    "/owners/get-all-branches",
                    "/owners/get-branch-by-id/{branchId}"

            ).hasAnyRole("TOP_OWNER", "OWNER");

            authRequest.requestMatchers(
                    "/owners/update-owner/{ownerId}",
                    "/owners/edit-profile/{ownerId}",
                    "/owners/delete-owner/{ownerId}",
                    "/owners/edit-profile/{ownerId}",
                    "/owners/add-branch",
                    "/owners/update-branch/{branchId}",
                    "/owners/delete-branch/{branchId}"

            ).hasRole("TOP_OWNER");





        });

        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(AbstractHttpConfigurer::disable);
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
