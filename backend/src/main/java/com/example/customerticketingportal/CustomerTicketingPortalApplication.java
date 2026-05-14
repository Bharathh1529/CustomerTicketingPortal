package com.example.customerticketingportal;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class CustomerTicketingPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerTicketingPortalApplication.class, args);
    }
}
