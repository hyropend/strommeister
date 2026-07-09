package dev.hyropend.sensorservice.controller;

import dev.hyropend.sensorservice.dto.FacilityRequest;
import dev.hyropend.sensorservice.dto.FacilityResponse;
import dev.hyropend.sensorservice.repository.SensorRepository;
import dev.hyropend.sensorservice.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService service;

    @PostMapping
    public ResponseEntity<FacilityResponse> createFacility(@Valid @RequestBody FacilityRequest request){
        FacilityResponse createdFacility = service.createFacility(request);
        return ResponseEntity.created(URI.create("/api/v1/facilities/" + createdFacility.id()))
                .body(createdFacility);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityResponse> getFacility(@PathVariable Long id){
        FacilityResponse facility = service.getFacility(id);
        return ResponseEntity.ok(facility);
    }

    @GetMapping()
    public List<FacilityResponse> getAllFacility(){
        //List<FacilityResponse> facilities = service.getAllFacility();
        //return ResponseEntity.ok(facilities);
        return service.getAllFacility();
    }
}
