package com.GP.ELsayes.security;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // React application's origin
        //configuration.setAllowedOrigins(Arrays.asList("*")); // React application's origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Important if you are sending cookies or using sessions
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeRequests(authorize -> authorize


                        .requestMatchers(
                                "/auth/login"
                                , "/customers/register"
                                ,"/owners/main-owner-is-exist"
                                ,"/owners/register-owner"
                                ,"/notifications/**"
                        ).permitAll() // Permit all for these endpoints


                        .requestMatchers("/customers/**").hasRole("CUSTOMER")

                        .requestMatchers("/notifications/**").hasAnyRole("CUSTOMER","PARKING_WORKER", "CLEANING_WORKER","MAINTENANCE_WORKER")


                        .requestMatchers(
                                "/workers/edit-profile/{workerId}"
                                , "/workers/change-worker-status/{workerId}"
                                ,"/finish-task"
                        ).hasAnyRole("PARKING_WORKER", "CLEANING_WORKER","MAINTENANCE_WORKER")
                        .requestMatchers(
                                "/workers/check-out"
                                , "/workers/generate-free-code/{workerId}"
                                , "/workers/record-visitation"
                        ).hasRole("PARKING_WORKER")


                        .requestMatchers(
                                "/managers/edit-profile/{managerId}",
                                "/managers/get-manager-by-id/{managerId}",
                                "/managers/add-worker",
                                "/managers/update-worker/{workerId}",
                                "/managers/delete-worker/{workerId}",
                                "/managers/get-worker-by-id/{workerId}",
                                "/managers/get-all-workers-by-branchId/{branchId}",
                                "/managers/get-customer-by-id/{customerId}",
                                "/managers/get-all-services",
                                "/managers/get-service-by-id/{serviceId}",
                                "/managers/activate-service-in-branch",
                                "/managers/deactivate-service-in-branch",
                                "/managers/get-all-service-by-branch-id/{branchId}",
                                "/managers/get-package-by-id/{packageId}",
                                "/managers/get-all-packages",
                                "/managers/activate-package-in-branch",
                                "/managers/deactivate-package-in-branch",
                                "/managers/get-all-packages-by-branch-id/{branchId}"
                        ).hasAnyRole("TOP_MANAGER", "MANAGER")
                        .requestMatchers(
                                "/managers/get-all-workers",
                                "/managers/update-customer/{customerId}",
                                "/managers/delete-customer/{customerId}",
                                "/managers/get-all-customers",
                                "/managers/add-service",
                                "/managers/add-service-to-branch",
                                "/managers/update-service/{serviceId}",
                                "/managers/delete-service/{serviceId}",
                                "/managers/add-service-to-package",
                                "/managers/add-package",
                                "/managers/add-package-to-branch",
                                "/managers/update-package/{packageId}",
                                "/managers/delete-package/{packageId}"
                        ).hasRole("TOP_MANAGER")


                        .requestMatchers(
                                "/owners/get-all-branches"
                        ).hasAnyRole("TOP_MANAGER", "OWNER")


                        .requestMatchers(
                            "/owners/add-owner",
                            "/owners/update-owner/{ownerId}",
                            "/owners/edit-profile/{ownerId}",
                            "/owners/delete-owner/{ownerId}",
                            "/owners/get-all",
                            "/owners/get-by-id/{ownerId}",
                            "/owners/add-manager",
                            "/owners/update-manager/{managerId}",
                            "/owners/delete-manager/{managerId}",
                            "/owners/get-all-managers",
                            "/owners/get-manager-by-id/{managerId}",
                            "/owners/get-manager-by-branchId/{branchId}",
                            "/owners/add-branch",
                            "/owners/update-branch/{branchId}",
                            "/owners/delete-branch/{branchId}",
                            "/owners/get-branch-by-id/{branchId}"

                        ).hasAnyRole("OWNER")


                        .requestMatchers(
                                "/notifications/get-by-id/{notificationId}",
                                "/notifications/get-all-notification-by-userId/{userId}",
                                "/notifications/delete/{notificationId}",
                                "/notifications/delete-all-by-user-id/{userId}",
                                "/notifications/delete-all-by-user-id/{userId}",
                                "/notifications/get-count-unOpened-by-user-id/{userId}"
                        ).hasAnyRole("PARKING_WORKER", "CLEANING_WORKER","MAINTENANCE_WORKER","CUSTOMER")

                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF if not used
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


}
