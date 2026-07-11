package dev.hyropend.sensorservice.repository;

import dev.hyropend.sensorservice.entity.Sensor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);

    //@EntityGraph(attributePaths = "facility")
    //@Query("select s from Sensor s")
    @Query("select s from Sensor s join fetch s.facility")
    List<Sensor> findAllWithFacility();
}
