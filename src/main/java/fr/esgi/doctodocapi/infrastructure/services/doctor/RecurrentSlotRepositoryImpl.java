package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.RecurrentSlotEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.RecurrentSlotJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.RecurrentSlotMapper;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlotRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the {@link RecurrentSlotRepository} interface.
 * <p>
 * Handles persistence of {@link RecurrentSlot} domain objects through JPA.
 */
@Service
public class RecurrentSlotRepositoryImpl implements RecurrentSlotRepository {
    private final RecurrentSlotJpaRepository recurrentSlotJpaRepository;
    private final RecurrentSlotMapper recurrentSlotMapper;

    public RecurrentSlotRepositoryImpl(RecurrentSlotJpaRepository recurrentSlotJpaRepository,
                                       RecurrentSlotMapper recurrentSlotMapper) {
        this.recurrentSlotJpaRepository = recurrentSlotJpaRepository;
        this.recurrentSlotMapper = recurrentSlotMapper;
    }

    /**
     * Persists a recurrent slot in the database.
     *
     * @param recurrentSlot the domain {@link RecurrentSlot} to save
     * @return the saved {@link RecurrentSlot} as a domain object
     */
    @Override
    public RecurrentSlot save(RecurrentSlot recurrentSlot) {
        RecurrentSlotEntity entity = recurrentSlotMapper.toEntity(recurrentSlot);
        RecurrentSlotEntity savedEntity = recurrentSlotJpaRepository.save(entity);
        return recurrentSlotMapper.toDomain(savedEntity);
    }

    /**
     * Finds a recurrent slot by its unique identifier.
     *
     * @param id the ID of the recurrent slot
     * @return an {@link Optional} containing the {@link RecurrentSlot} if found, or empty otherwise
     */
    @Override
    public Optional<RecurrentSlot> findById(UUID id) {
        return this.recurrentSlotJpaRepository.findById(id).map(recurrentSlotMapper::toDomain);
    }
}
