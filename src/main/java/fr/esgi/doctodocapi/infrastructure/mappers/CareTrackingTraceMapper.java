package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingTraceEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import org.springframework.stereotype.Service;

@Service
public class CareTrackingTraceMapper {
    public CareTrackingTrace toDomain(CareTrackingTraceEntity entity) {
        return new CareTrackingTrace(
                entity.getId(),
                entity.getConsultedBy().getId(),
                entity.getConsultedAt()
        );
    }

    public CareTrackingTraceEntity toEntity(CareTrackingTrace domain, CareTrackingEntity careTrackingEntity) {
        CareTrackingTraceEntity entity = new CareTrackingTraceEntity();

        entity.setId(domain.getId());
        entity.setCareTracking(careTrackingEntity);
        entity.setConsultedAt(domain.getConsultedAt());

        return entity;
    }
}