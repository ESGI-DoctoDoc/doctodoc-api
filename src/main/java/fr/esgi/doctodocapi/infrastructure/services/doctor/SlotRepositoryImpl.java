package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.SlotJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.SlotMapper;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SlotRepositoryImpl implements SlotRepository {
    private final SlotJpaRepository slotJpaRepository;
    private final SlotMapper slotMapper;

    public SlotRepositoryImpl(SlotJpaRepository slotJpaRepository, SlotMapper slotMapper) {
        this.slotJpaRepository = slotJpaRepository;
        this.slotMapper = slotMapper;
    }

    @Override
    public List<Slot> getSlotsByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date) {
        List<SlotEntity> slotsFoundByMedicalConcern = this.slotJpaRepository.findAllByMedicalConcerns_IdAndDate(medicalConcernId, date);
        return slotsFoundByMedicalConcern.stream().map(slotMapper::toDomain).toList();
    }
}
