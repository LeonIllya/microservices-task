package org.serviceA.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient dataApiRestClient(
            @Value("${data-api.url}") String dataApiUrl
    ) {
        return RestClient.builder()
                .baseUrl(dataApiUrl)
                .build();
    }
}
