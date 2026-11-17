package org.serviceA.service.impl;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.serviceA.client.DataApiClient;
import org.serviceA.dto.process.TransformResponse;
import org.serviceA.model.ProcessingLog;
import org.serviceA.model.User;
import org.serviceA.repository.ProcessingRepository;
import org.serviceA.repository.UserRepository;
import org.serviceA.service.ProcessService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final DataApiClient dataApiClient;
    private final ProcessingRepository processingRepository;
    private final UserRepository userRepository;

    public TransformResponse process(String text) {
        TransformResponse response = dataApiClient.transform(text);
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ProcessingLog processingLog = new ProcessingLog();
        processingLog.setUser(user);
        processingLog.setInputText(text);
        processingLog.setOutputText(response.result());
        processingLog.setCreatedAt(LocalDateTime.now());

        processingRepository.save(processingLog);

        return response;
    }
}