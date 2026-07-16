package dev.hyropend.ingestion_service.component;

import dev.hyropend.ingestion_service.dto.EnergyReading;
import dev.hyropend.ingestion_service.entity.Reading;
import dev.hyropend.ingestion_service.repository.ReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReadingConsumer {
    private final ReadingRepository repository;

    @KafkaListener(topics = "energy.readings", groupId = "ingestion-group")
    public void consume(EnergyReading reading){
        log.info("Received: {}", reading);
        try{
            repository.save(new Reading(reading.sensorId(),
                    reading.powerKw(),
                    reading.timestamp()));
        }
        catch (DataIntegrityViolationException e){
            log.warn("Duplicate reading ignored: {} @ {}", reading.sensorId(), reading.timestamp());
        }
    }
}
