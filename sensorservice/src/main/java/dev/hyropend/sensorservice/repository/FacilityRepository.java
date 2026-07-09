package dev.hyropend.sensorservice.repository;

import dev.hyropend.sensorservice.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    boolean existsByName(String name);
    boolean existsById(Long id);
}
