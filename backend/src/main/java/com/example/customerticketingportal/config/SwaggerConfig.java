package com.example.customerticketingportal.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiDetails() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Ticketing Portal API")
                        .description("API documentation for Ticket, Users, SLA, KB Articles and Messages")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bharathkumar")
                                 .email("example@email.com")
                                .url("https://yourwebsite.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}