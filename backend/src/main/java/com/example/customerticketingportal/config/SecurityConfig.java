    package com.example.customerticketingportal.config;

    import com.example.customerticketingportal.Security.JwtAuthFilter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.web.SecurityFilterChain;

    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;

    @Configuration
    public class SecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;

        public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
            this.jwtAuthFilter = jwtAuthFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                    .csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .authorizeHttpRequests(auth -> auth

                            // ✅ PUBLIC ENDPOINTS
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/setup/**").permitAll()

                            // ✅ Swagger Public (important)
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/v3/api-docs.yaml",
                                    "/swagger-resources/**",
                                    "/webjars/**"
                            ).permitAll()
                            .requestMatchers(HttpMethod.POST, "/tickets").permitAll()
                            .requestMatchers("/tickets/requester/**")
                            .hasAnyRole("USER", "AGENT", "SUPERVISOR", "ADMIN")

                            .requestMatchers("/tickets/**")
                            .hasAnyRole("USER","AGENT", "SUPERVISOR", "ADMIN")

                            .requestMatchers("/tickets/agents/**")
                            .hasAnyRole("USER","AGENT", "SUPERVISOR")

                            .requestMatchers("/tickets/assignee/**")
                            .hasAnyRole("AGENT","SUPERVISOR")

                            .requestMatchers("/kb/articles/**")
                            .hasAnyRole("AGENT", "SUPERVISOR", "USER")

                            .requestMatchers("/kb/search/**")
                            .hasAnyRole("AGENT", "SUPERVISOR", "USER")

                            .requestMatchers("/tickets/agent/dashboard/**")
                            .hasAnyRole("AGENT","SUPERVISOR")
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        @Bean
        public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();

            config.setAllowedOrigins(List.of("*"));  // OR put specific frontend URL
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setExposedHeaders(List.of("Authorization"));
            config.setAllowCredentials(false);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);

            return source;
        }
    }