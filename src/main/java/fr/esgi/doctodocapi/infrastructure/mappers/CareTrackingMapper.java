package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareTrackingMapper {
    public CareTracking toDomain(CareTrackingEntity entity, Doctor doctor, Patient patient, List<Appointment> appointments, List<CareTrackingTrace> careTrackingTraces) {
        return new CareTracking(
                entity.getId(),
                entity.getCaseName(),
                entity.getDescription(),
                doctor,
                patient,
                entity.getDocuments(),
                entity.getDoctors(),
                appointments,
                careTrackingTraces,
                entity.getCreatedAt(),
                entity.getClosedAt() == null ? null : entity.getClosedAt()
        );
    }

    public CareTrackingEntity toEntity(CareTracking domain, DoctorEntity creatorEntity, PatientEntity patientEntity) {
        CareTrackingEntity entity = new CareTrackingEntity();

        entity.setId(domain.getId());
        entity.setCaseName(domain.getCaseName());
        entity.setDescription(domain.getDescription());
        entity.setCreator(creatorEntity);
        entity.setPatient(patientEntity);
        entity.setDoctors(domain.getDoctors());
        entity.setDocuments(domain.getDocuments());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setClosedAt(domain.getClosedAt());

        return entity;
    }
}
