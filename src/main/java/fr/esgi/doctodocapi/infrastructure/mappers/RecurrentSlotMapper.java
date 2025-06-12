package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrenceType;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlot;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.RecurrentSlotEntity;
import org.springframework.stereotype.Service;

@Service
public class RecurrentSlotMapper {
    public RecurrentSlot toDomain(RecurrentSlotEntity entity) {
        return new RecurrentSlot(
                entity.getId(),
                RecurrenceType.fromValue(entity.getType()),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCreatedAt()
        );
    }

    public RecurrentSlotEntity toEntity(RecurrentSlot recurrentSlot) {
        RecurrentSlotEntity entity = new RecurrentSlotEntity();
        entity.setType(recurrentSlot.getType().getValue());
        entity.setStartDate(recurrentSlot.getStartDate());
        entity.setEndDate(recurrentSlot.getEndDate());
        entity.setCreatedAt(recurrentSlot.getCreatedAt());
        return entity;
    }
}
