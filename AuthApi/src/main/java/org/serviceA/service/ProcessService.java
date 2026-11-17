package org.serviceA.service;

import org.serviceA.dto.process.TransformResponse;

public interface ProcessService {
    TransformResponse process(String text);
}
