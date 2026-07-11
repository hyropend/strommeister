package dev.hyropend.sensorservice.repository;

import dev.hyropend.sensorservice.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
