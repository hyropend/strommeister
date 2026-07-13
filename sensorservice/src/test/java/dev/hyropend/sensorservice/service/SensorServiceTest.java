package dev.hyropend.sensorservice.service;

import dev.hyropend.sensorservice.dto.CreateSensorRequest;
import dev.hyropend.sensorservice.dto.SensorResponse;
import dev.hyropend.sensorservice.entity.Facility;
import dev.hyropend.sensorservice.entity.Sensor;
import dev.hyropend.sensorservice.entity.SensorStatus;
import dev.hyropend.sensorservice.exception.DuplicateSensorException;
import dev.hyropend.sensorservice.exception.FacilityNotFoundException;
import dev.hyropend.sensorservice.repository.FacilityRepository;
import dev.hyropend.sensorservice.repository.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private SensorService sensorService;

    @Test
    void createSensor_shouldThrowException_whenSerialNumberAlreadyExists(){

        // given
        CreateSensorRequest request = new CreateSensorRequest("SN-001", "Verteilerkasten 3", 1L);
        when(sensorRepository.existsBySerialNumber("SN-001")).thenReturn(true);

        // when + then
        assertThrows(DuplicateSensorException.class,
                () -> sensorService.createSensor(request));
        verify(sensorRepository, never()).save(any());
    }

    @Test
    void createSensor_shouldThrowException_whenFacilityNotFound(){
        //given
        CreateSensorRequest request = new CreateSensorRequest("SN-001", "Verteilerkasten 3", 99L);
        when(sensorRepository.existsBySerialNumber("SN-001")).thenReturn(false); // pass
        when(facilityRepository.findById(99L)).thenReturn(Optional.empty());

        //when + then
        assertThrows(FacilityNotFoundException.class,
                ()-> sensorService.createSensor(request));
        verify(sensorRepository, never()).save(any());
    }

    @Test
    void createSensor_shouldSaveAndReturnResponse_whenRequestIsValid(){
        CreateSensorRequest request = new CreateSensorRequest("SN-001", "Verteilerkasten 3", 1L);
        when(sensorRepository.existsBySerialNumber("SN-001")).thenReturn(false);

        Facility facility = new Facility("Halle A", "adres");
        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));

        Sensor savedSensor = new Sensor("SN-001","Verteilerkasten 3", facility);
        when(sensorRepository.save(any())).thenReturn(savedSensor);

        // when
        SensorResponse response = sensorService.createSensor(request);

        // then
        assertNotNull(response);
        assertEquals("SN-001", response.serialNumber());
        assertEquals(SensorStatus.ACTIVE, response.status());
        verify(sensorRepository).save(any());
        verify(auditService).log(anyString());
    }
}
