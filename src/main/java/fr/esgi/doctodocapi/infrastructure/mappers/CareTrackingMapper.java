package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareTrackingMapper {
    public CareTracking toDomain(CareTrackingEntity entity, Patient patient, List<CareTrackingTrace> careTrackingTraces, List<Document> documents) {
        return new CareTracking(
                entity.getId(),
                entity.getCaseName(),
                entity.getDescription(),
                entity.getCreator().getId(),
                patient,
                documents,
                entity.getDoctors(),
                entity.getAppointments().stream().map(AppointmentEntity::getId).toList(),
                careTrackingTraces,
                entity.getCreatedAt(),
                entity.getClosedAt() == null ? null : entity.getClosedAt()
        );
    }

    public CareTrackingEntity toEntity(CareTracking domain, PatientEntity patientEntity) {
        CareTrackingEntity entity = new CareTrackingEntity();

        entity.setCaseName(domain.getCaseName());
        entity.setDescription(domain.getDescription());
        entity.setPatient(patientEntity);
        entity.setDoctors(domain.getDoctors());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setClosedAt(domain.getClosedAt());

        return entity;
    }
}
