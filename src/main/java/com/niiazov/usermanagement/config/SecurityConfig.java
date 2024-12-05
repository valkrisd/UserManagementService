package com.niiazov.usermanagement.config;

import com.niiazov.usermanagement.security.JwtAuthEntryPoint;
import com.niiazov.usermanagement.security.JwtAuthentificationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthEntryPoint authEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthEntryPoint authEntryPoint) {
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/users/{userId}/profiles",
                                "/users/{userId}/activation-token",
                                "/users/activate",
                                "/users/me"
                        ).authenticated()
                        .requestMatchers("/users/{userId}/roles/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/users/{userId}/courses",
                                "/users/enrollments",
                                "/users/{userId}/enrollments/{enrollmentId}"
                        ).hasRole("USER")
                        .requestMatchers(
                                "/users/register",
                                "/users/login"
                        ).anonymous()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                // sessionManagement: This determines security policies related to HTTP sessions.
                // These settings specify how user sessions are managed.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // exceptionHandling: This addresses exceptions that may occur during authentication
                // and generates an appropriate response.
                .exceptionHandling((exceptions) -> exceptions.authenticationEntryPoint(authEntryPoint))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordREncode() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthentificationFilter jwtAuthFilter() {
        return new JwtAuthentificationFilter();
    }
}

