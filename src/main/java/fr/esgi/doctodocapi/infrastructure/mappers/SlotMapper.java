package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrenceType;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotMapper {
    public Slot toDomain(SlotEntity entity, List<Appointment> appointments, List<MedicalConcern> medicalConcerns) {
        return new Slot(
                entity.getId(),
                entity.getDate(),
                entity.getStartHour(),
                entity.getEndHour(),
                appointments,
                medicalConcerns,
                RecurrenceType.fromValue(entity.getRecurrenceType()),
                entity.getDoctor().getId()
        );
    }

    public Slot toDomain(SlotEntity entity) {
        return new Slot(
                entity.getId(),
                entity.getDate(),
                entity.getStartHour(),
                entity.getEndHour()
        );
    }

    public SlotEntity toEntity(Slot slot, List<AppointmentEntity> appointments, List<MedicalConcernEntity> medicalConcerns, DoctorEntity doctor) {
        SlotEntity entity = new SlotEntity();
        entity.setDate(slot.getDate());
        entity.setStartHour(slot.getHoursRange().getStart());
        entity.setEndHour(slot.getHoursRange().getEnd());
        entity.setRecurrenceType(slot.getRecurrenceType().getValue());
        entity.setMedicalConcerns(medicalConcerns);
        entity.setAppointments(appointments);
        entity.setDoctor(doctor);
        return entity;
    }
}
