package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMapper {
    private final SlotMapper slotMapper;
    private final PatientMapper patientMapper;
    private final MedicalConcernMapper medicalConcernMapper;

    public AppointmentMapper(SlotMapper slotMapper, PatientMapper patientMapper, MedicalConcernMapper medicalConcernMapper) {
        this.slotMapper = slotMapper;
        this.patientMapper = patientMapper;
        this.medicalConcernMapper = medicalConcernMapper;
    }

    public Appointment toDomain(AppointmentEntity entity) {
        return new Appointment(
                entity.getId(),
                this.slotMapper.toDomain(entity.getSlot()),
                this.patientMapper.toDomain(entity.getPatient()),
                this.medicalConcernMapper.toDomain(entity.getMedicalConcern()),
                entity.getStartHour(),
                entity.getEndHour(),
                entity.getTakenAt(),
                entity.getStatus()
        );
    }
}
