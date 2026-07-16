package dev.hyropend.ingestion_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "readings",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"sensorId", "timestamp"})
        }
        )
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sensorId;

    @Column(nullable = false)
    private double powerKw;

    @Column(nullable = false)
    private Instant timestamp;

    public Reading(String sensorId, double powerKw, Instant timestamp){
        this.sensorId=sensorId;
        this.powerKw=powerKw;
        this.timestamp=timestamp;
    }
}
