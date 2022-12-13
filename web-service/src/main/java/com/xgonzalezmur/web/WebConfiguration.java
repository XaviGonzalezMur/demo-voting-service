package com.xgonzalezmur.web;

import com.xgonzalezmur.web.controller.WebController;
import com.xgonzalezmur.web.service.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class WebConfiguration {

    // Case-insensitive: could also use: http://voting-service
    public static final String VOTING_SERVICE_URL = "http://VOTING-SERVICE";

    @Bean
    @LoadBalanced    // Make sure to create the load-balanced template
    public RestTemplate restTemplate() {
        log.debug("Instantiating a Bean of RestTemplate");
        return new RestTemplate();
    }

    @Bean
    public WebService webService() {
        return new WebService(VOTING_SERVICE_URL, restTemplate());
    }

    @Bean
    public WebController webController() {
        return new WebController(webService());
    }
}
