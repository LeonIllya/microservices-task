package org.serviceA.repository;

import java.util.UUID;

import org.serviceA.model.ProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingRepository extends JpaRepository<ProcessingLog, UUID> { }
