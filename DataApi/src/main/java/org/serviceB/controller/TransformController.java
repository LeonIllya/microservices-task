package org.serviceB.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.serviceB.dto.TransformRequest;
import org.serviceB.dto.TransformResponse;
import org.serviceB.service.TransformService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TransformController {

    private final TransformService transformService;

    @PostMapping("/transform")
    public TransformResponse transform(
            @Valid @RequestBody TransformRequest request
    ) {
        String result = transformService.transform(request.text());
        return new TransformResponse(result);
    }
}
