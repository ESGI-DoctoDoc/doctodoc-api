package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
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
                medicalConcerns
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

    public SlotEntity toEntity(Slot slot) {
        SlotEntity entity = new SlotEntity();
        entity.setDate(slot.getDate());
        entity.setStartHour(slot.getHoursRange().getStart());
        entity.setEndHour(slot.getHoursRange().getEnd());
        return entity;
    }
}
