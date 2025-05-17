package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import org.springframework.stereotype.Service;

@Service
public class SlotMapper {
    private final MedicalConcernMapper medicalConcernMapper;

    public SlotMapper(MedicalConcernMapper medicalConcernMapper) {
        this.medicalConcernMapper = medicalConcernMapper;
    }

    public Slot toDomain(SlotEntity entity) {
        return new Slot(
                entity.getId(),
                entity.getDate(),
                entity.getStartHour().toLocalTime(),
                entity.getEndHour().toLocalTime(),
                entity.getMedicalConcerns().stream().map(medicalConcernMapper::toDomain).toList()
        );
    }
}
