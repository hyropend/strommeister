package dev.hyropend.sensorservice.service;

import dev.hyropend.sensorservice.dto.FacilityRequest;
import dev.hyropend.sensorservice.dto.FacilityResponse;
import dev.hyropend.sensorservice.entity.Facility;
import dev.hyropend.sensorservice.exception.DuplicateFacilityException;
import dev.hyropend.sensorservice.exception.FacilityNotFoundException;
import dev.hyropend.sensorservice.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository repository;

    public FacilityResponse createFacility(FacilityRequest request){
        if(repository.existsByName(request.name())){
            throw new DuplicateFacilityException(request.name());
        }

        Facility facility = new Facility(request.name(), request.address());

        Facility saved = repository.save(facility);
        return toResponse(saved);
    }

    public FacilityResponse getFacility(Long id) {
        Facility facility = repository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException(id));
        return toResponse(facility);
    }

    public List<FacilityResponse> getAllFacility() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FacilityResponse toResponse(Facility facility){
        return new FacilityResponse(facility.getId(),
                facility.getName(),
                facility.getAddress(),
                facility.getCreatedAt());
    }

}
