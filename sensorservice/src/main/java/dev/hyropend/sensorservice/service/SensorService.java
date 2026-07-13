package dev.hyropend.sensorservice.service;

import dev.hyropend.sensorservice.dto.CreateSensorRequest;
import dev.hyropend.sensorservice.dto.FacilityResponse;
import dev.hyropend.sensorservice.dto.SensorResponse;
import dev.hyropend.sensorservice.entity.Facility;
import dev.hyropend.sensorservice.entity.Sensor;
import dev.hyropend.sensorservice.entity.SensorStatus;
import dev.hyropend.sensorservice.exception.DuplicateSensorException;
import dev.hyropend.sensorservice.exception.FacilityNotFoundException;
import dev.hyropend.sensorservice.repository.FacilityRepository;
import dev.hyropend.sensorservice.repository.SensorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository repository;
    private final FacilityRepository facilityRepository;
    private final AuditService auditService;

    @Transactional
    public SensorResponse createSensor(CreateSensorRequest request){
        if(repository.existsBySerialNumber(request.serialNumber())){
            throw new DuplicateSensorException(request.serialNumber());
        }

        Facility facility = facilityRepository.findById(request.facilityId())
                .orElseThrow(() -> new FacilityNotFoundException(request.facilityId()));

        Sensor sensor = new Sensor(request.serialNumber(), request.location(), facility);
        sensor.setStatus(SensorStatus.ACTIVE);
        sensor.setInstalledAt(Instant.now());
        sensor.setFacility(facility);

        Sensor saved = repository.save(sensor);
        auditService.log("SENSOR_CREATED: " + saved.getSerialNumber());
        //throw new RuntimeException("test: kayıttan sonra patlama");
        return toResponse(saved);
    }

    private SensorResponse toResponse(Sensor s){
        return new SensorResponse(
                s.getId(), s.getSerialNumber(), s.getLocation(),
                s.getStatus(), s.getInstalledAt(),
                s.getFacility().getId(),
                s.getFacility().getName()
        );
    }

    public List<SensorResponse> getAllSensors() {
       /* return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList(); */
        return repository.findAllWithFacility()
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public List<SensorResponse> slowGetAll() throws InterruptedException {
        Thread.sleep(5000);
        return repository.findAllWithFacility().stream().map(this::toResponse).toList();
    }
}
