package org.serviceA.client;

import org.serviceA.dto.process.TransformRequest;
import org.serviceA.dto.process.TransformResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DataApiClient {

    private final RestClient restClient;

    @Value("${internal.token}")
    private String internalToken;

    public DataApiClient(RestClient dataApiRestClient) {
        this.restClient = dataApiRestClient;
    }

    public TransformResponse transform(String text) {
        return restClient.post()
                .uri("/api/transform")
                .header("X-Internal-Token", internalToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TransformRequest(text))
                .retrieve()
                .body(TransformResponse.class);
    }
}
