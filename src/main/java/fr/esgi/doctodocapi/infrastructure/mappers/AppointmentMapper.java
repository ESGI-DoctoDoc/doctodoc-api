package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.domain.entities.appointment.Appointment;
import fr.esgi.doctodocapi.domain.entities.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.domain.entities.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.domain.entities.doctor.Doctor;
import fr.esgi.doctodocapi.domain.entities.doctor.calendar.Slot;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.domain.entities.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentMapper {
    public Appointment toDomain(AppointmentEntity entity, Slot slot, Patient patient, Doctor doctor, MedicalConcern medicalConcern, List<PreAppointmentAnswers> answers) {
        return new Appointment(
                entity.getId(),
                slot,
                patient,
                doctor,
                medicalConcern,
                entity.getStartHour(),
                entity.getEndHour(),
                entity.getTakenAt(),
                AppointmentStatus.valueOf(entity.getStatus()),
                answers,
                entity.getLockedAt()
        );
    }

    public AppointmentEntity toEntity(Appointment appointment, SlotEntity slotEntity, PatientEntity patientEntity, DoctorEntity doctorEntity, MedicalConcernEntity medicalConcernEntity) {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setSlot(slotEntity);
        entity.setPatient(patientEntity);
        entity.setDoctor(doctorEntity);
        entity.setMedicalConcern(medicalConcernEntity);
        entity.setDate(appointment.getDate());
        entity.setStartHour(appointment.getHoursRange().getStart());
        entity.setEndHour(appointment.getHoursRange().getEnd());
        entity.setTakenAt(appointment.getTakenAt());
        entity.setStatus(appointment.getStatus().name());
        entity.setLockedAt(appointment.getLockedAt());
        return entity;
    }
}
