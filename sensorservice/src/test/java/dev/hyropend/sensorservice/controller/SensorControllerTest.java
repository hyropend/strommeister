package dev.hyropend.sensorservice.controller;

import dev.hyropend.sensorservice.dto.SensorResponse;
import dev.hyropend.sensorservice.entity.SensorStatus;
import dev.hyropend.sensorservice.exception.DuplicateSensorException;
import dev.hyropend.sensorservice.exception.FacilityNotFoundException;
import dev.hyropend.sensorservice.service.SensorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorController.class)
public class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorService sensorService;

    @Test
    void createSensor_shouldReturn201_whenRequestIsValid() throws Exception {
        SensorResponse response = new SensorResponse(1L, "SN-001", "Verteilerkasten 3",
                SensorStatus.ACTIVE, Instant.now(), 1L, "Halle A");
        when(sensorService.createSensor(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "serialNumber": "SN-001",
                                  "location": "Verteilerkasten 3",
                                  "facilityId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("SN-001"))
                .andExpect(jsonPath("$.facilityName").value("Halle A"));
    }

    @Test
    void createSensor_shouldReturn409_whenSerialNumberExists() throws Exception {
//        SensorResponse response = new SensorResponse(1L, "SN-001", "Verteilerkasten 3",
//                SensorStatus.ACTIVE, Instant.now(), 1L, "Halle A");
        when(sensorService.createSensor(any())).thenThrow(new DuplicateSensorException("SN-001"));

        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "serialNumber": "SN-001",
                                  "location": "Verteilerkasten 3",
                                  "facilityId": 1
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void createSensor_shouldReturn404_whenFacilityNotFound() throws Exception {
//        SensorResponse response = new SensorResponse(1L, "SN-001", "Verteilerkasten 3",
//                SensorStatus.ACTIVE, Instant.now(), 1L, "Halle A");
        when(sensorService.createSensor(any())).thenThrow(new FacilityNotFoundException(99L));

        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "serialNumber": "SN-001",
                                  "location": "Verteilerkasten 3",
                                  "facilityId": 1
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void createSensor_shouldReturn400_whenSerialNumberIsBlank() throws Exception {
//        SensorResponse response = new SensorResponse(1L, "SN-001", "Verteilerkasten 3",
//                SensorStatus.ACTIVE, Instant.now(), 1L, "Halle A");
//        when(sensorService.createSensor(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "serialNumber": "",
                                  "location": "Verteilerkasten 3",
                                  "facilityId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
        verify(sensorService, never()).createSensor(any());
    }
}
