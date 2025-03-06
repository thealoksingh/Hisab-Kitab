package com.hisabKitab.springProject.security;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@EnableMethodSecurity
@Configuration
public class JwtSecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private CustomAuthEntryPoint customAuthEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","https://hisab-kitab-business.netlify.app" )); // Frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Global CORS config
        .csrf(csrf -> csrf.disable()) // Disable CSRF as it's not needed for stateless APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                                                                                                              // session,
                                                                                                              // as
                                                                                                              // we're
                                                                                                              // using
                                                                                                              // JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**", "/api/ping", "/user/login", "/user/signup", "/user/refresh-token",
                                "/user/update-password", "/user/sendOTP", "/actuator",
                                "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll() // Public endpoints
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow OPTIONS for CORS preflight

                        // // Restrict access to /user/** paths to users with authority USER
                        // .requestMatchers("/user/**").hasRole("ADMIN")

                        // // Restrict access to /admin/** paths to users with authority ADMIN
                        // .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()) // All other requests need to be authenticated
                // filter for JWT

                // added for ngrok link
                // .headers(headers -> headers.frameOptions(frameOptions ->
                // frameOptions.disable()))

                .authenticationProvider(authenticationProvider()) // validation
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add custom
                // .oauth2ResourceServer(oauth2 -> oauth2
                // .jwt()) // Enable JWT authentication
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthEntryPoint) // Custom authentication entry point
                        .accessDeniedHandler(customAccessDeniedHandler)) // Handle access denied for
                // // insufficient privileges
                .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP basic authentication (we're using JWT)

                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new org.springframework.security.authentication.ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        var secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        var jwkSource = new ImmutableSecret<>(secretKey);
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secret.getBytes(), "HmacSHA256")).build();
    }

}
