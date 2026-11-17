package org.serviceA.controller;

import lombok.RequiredArgsConstructor;

import org.serviceA.dto.process.TransformRequest;
import org.serviceA.dto.process.TransformResponse;
import org.serviceA.service.ProcessService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    @PostMapping
    public TransformResponse process(@RequestBody TransformRequest request) {
        return processService.process(request.text());
    }
}
