package dev.hyropend.sensorservice.service;

import dev.hyropend.sensorservice.entity.AuditLog;
import dev.hyropend.sensorservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String action){
        repository.save(new AuditLog(action));
    }
}
