package dev.hyropend.sensorservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorStatus status;

    private Instant installedAt;

    public Sensor(String serialNumber, String location, Facility facility) {
        this.serialNumber = serialNumber;
        this.location = location;
        this.facility = facility;
    }
}
