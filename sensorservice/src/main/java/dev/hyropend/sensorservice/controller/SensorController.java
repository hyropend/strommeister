package dev.hyropend.sensorservice.controller;

import dev.hyropend.sensorservice.dto.CreateSensorRequest;
import dev.hyropend.sensorservice.dto.SensorResponse;
import dev.hyropend.sensorservice.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService service;

    @PostMapping
    public ResponseEntity<SensorResponse> create (@Valid @RequestBody CreateSensorRequest request){
        SensorResponse created = service.createSensor(request);
        return ResponseEntity.created(URI.create("/api/v1/sensors/" + created.id()))
                .body(created);
    }

    @GetMapping()
    public List<SensorResponse> getAllSensors(){
        //List<SensorResponse> sensors = service.getAllSensors();
        //return ResponseEntity.ok(sensors);
        return service.getAllSensors();
    }

    @GetMapping("/slow")
    public List<SensorResponse> slow() throws InterruptedException {
        return service.slowGetAll();
    }
}
