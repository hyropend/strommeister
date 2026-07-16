package dev.hyropend.sensorsimulator.component;

import dev.hyropend.sensorsimulator.dto.EnergyReading;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReadingProducer {
    private final KafkaTemplate<String, EnergyReading> kafkaTemplate;
    //private final Random random;
    private final Random random = new Random();

    private static final List<String> SENSOR_IDS = List.of("SN-001", "SN-002", "SN-003", "SN-004",
            "SN-005", "SN-006","SN-007","SN-008");

    @Scheduled(fixedRate = 3000)
    public void ProduceReadings(){
        for(String sensorId : SENSOR_IDS){
            EnergyReading reading = new EnergyReading(sensorId, generatePower(), Instant.now());
            kafkaTemplate.send("energy.readings",sensorId,reading);
            log.info("Sent: {}", reading);
        }
    }

    private double generatePower(){
        int hour = LocalTime.now().getHour();
        double base = (hour >= 7 && hour <= 19) ? 40.0 : 12.0;
        return base + random.nextDouble() * 10; //noise
    }
}
