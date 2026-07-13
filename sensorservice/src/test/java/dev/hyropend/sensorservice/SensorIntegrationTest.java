package dev.hyropend.sensorservice;

import dev.hyropend.sensorservice.entity.Facility;
import dev.hyropend.sensorservice.repository.FacilityRepository;
import dev.hyropend.sensorservice.repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ImportTestcontainers
@AutoConfigureMockMvc
public class SensorIntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @BeforeEach
    void cleanDb() {
        sensorRepository.deleteAll();
        facilityRepository.deleteAll();
    }

    @Test
    void createSensor_shouldPersistToDatabase_withGeneratedId() throws Exception {
        Facility facility = facilityRepository.save(new Facility("Halle A", "Freising"));

        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "serialNumber": "SN-001", "location": "VK 3", "facilityId": %d }
                                """.formatted(facility.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());

        assertEquals(1, sensorRepository.count());
    }

    @Test
    void createSensor_shouldReturn409_whenSerialNumberAlreadyExistsInDb() throws Exception {
        Facility facility = facilityRepository.save(new Facility("Halle A", "Freising"));

        String body = """
            { "serialNumber": "SN-001", "location": "VK 3", "facilityId": %d }
            """.formatted(facility.getId());

        // İlk POST — kayıt başarılı
        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        // AYNI body ile ikinci POST — gerçek çakışma
        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("already exists")));

        assertEquals(1, sensorRepository.count());
    }

    @Test
    void getAllSensors_shouldReturnSensorsWithFacilityName() throws Exception {
        Facility facility = facilityRepository.save(new Facility("Halle A", "Freising"));

        mockMvc.perform(post("/api/v1/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "serialNumber": "SN-001", "location": "VK 3", "facilityId": %d }
                            """.formatted(facility.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/sensors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].serialNumber").value("SN-001"))
                .andExpect(jsonPath("$[0].facilityName").value("Halle A"));
    }
}
