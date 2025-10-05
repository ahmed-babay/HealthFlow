package com.appointment.service.appointmentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @Value("${doctor.service.url}")
    private String doctorServiceUrl;

    @Bean
    public WebClient patientServiceWebClient() {
        return WebClient.builder()
                .baseUrl(patientServiceUrl)
                .build();
    }

    @Bean
    public WebClient doctorServiceWebClient() {
        return WebClient.builder()
                .baseUrl(doctorServiceUrl)
                .build();
    }
}
