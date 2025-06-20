package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "care_tracking_traces")
public class CareTrackingTraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "care_tracking_trace_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "care_tracking_id", nullable = false)
    private CareTrackingEntity careTracking;
}
