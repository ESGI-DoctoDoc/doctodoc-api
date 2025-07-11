package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.MedicalConcernJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.SlotJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.MedicalConcernMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.SlotMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the SlotRepository interface.
 * This service provides methods to manage doctor appointment slots,
 * allowing the application to retrieve slot information based on various criteria.
 */
@Repository
public class SlotRepositoryImpl implements SlotRepository {
    /**
     * Repository for accessing slot data in the database.
     */
    private final SlotJpaRepository slotJpaRepository;

    /**
     * Mapper for converting between slot domain objects and entities.
     */
    private final SlotMapper slotMapper;

    /**
     * Facade mapper for converting between appointment entities and domain objects.
     */
    private final AppointmentFacadeMapper appointmentFacadeMapper;

    /**
     * Mapper for converting between medical concern domain objects and entities.
     */
    private final MedicalConcernMapper medicalConcernMapper;

    /**
     * Repository for accessing medical concern data in the database.
     */
    private final MedicalConcernJpaRepository medicalConcernJpaRepository;

    private final EntityManager entityManager;

    /**
     * Constructs a SlotRepositoryImpl with the required repositories and mappers.
     *
     * @param slotJpaRepository       Repository for slot data access
     * @param slotMapper              Mapper for slot domain objects and entities
     * @param appointmentFacadeMapper Facade mapper for appointment entities and domain objects
     * @param medicalConcernMapper    Mapper for medical concern domain objects and entities
     */
    public SlotRepositoryImpl(SlotJpaRepository slotJpaRepository, SlotMapper slotMapper, AppointmentFacadeMapper appointmentFacadeMapper, MedicalConcernMapper medicalConcernMapper, MedicalConcernJpaRepository medicalConcernJpaRepository, EntityManager entityManager) {
        this.slotJpaRepository = slotJpaRepository;
        this.slotMapper = slotMapper;
        this.appointmentFacadeMapper = appointmentFacadeMapper;
        this.medicalConcernMapper = medicalConcernMapper;
        this.medicalConcernJpaRepository = medicalConcernJpaRepository;
        this.entityManager = entityManager;
    }

    /**
     * Retrieves all slots associated with a specific medical concern on a given date.
     * This method maps the slot entities to domain objects, including their associated appointments and medical concerns.
     *
     * @param medicalConcernId The unique identifier of the medical concern
     * @param date The date for which to retrieve slots
     * @return A list of slots associated with the specified medical concern and date
     */
    @Override
    public List<Slot> getSlotsByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date) {
        List<SlotEntity> slotsFoundByMedicalConcern = this.slotJpaRepository.findAllByMedicalConcernIdAndDateAndMedicalConcernNotDeleted(medicalConcernId, date);

        return slotsFoundByMedicalConcern.stream()
                .map(this::mapSlotEntityToDomain)
                .toList();
    }

    /**
     * Retrieves a slot by its unique identifier.
     * This method maps the slot entity to a domain object, including its associated appointments and medical concerns.
     *
     * @param id The unique identifier of the slot to retrieve
     * @return The slot with the specified ID
     * @throws SlotNotFoundException If no slot with the specified ID exists
     */
    @Override
    public Slot getById(UUID id) {
        SlotEntity entity = this.slotJpaRepository.findById(id).orElseThrow(SlotNotFoundException::new);
        return mapSlotEntityToDomain(entity);
    }


    /**
     * Saves a list of slots to the database.
     *
     * @param slots the list of domain {@link Slot} objects to persist
     * @return the list of saved {@link Slot} objects
     */
    @Override
    public List<Slot> saveAll(List<Slot> slots, UUID doctorId) {
        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setId(doctorId);

        List<SlotEntity> entities = slots.stream()
                .map(slot -> mapSlotToEntity(slot, doctorEntity))
                .toList();

        List<SlotEntity> savedEntities = this.slotJpaRepository.saveAll(entities);
        return savedEntities.stream().map(this.slotMapper::toDomain).toList();
    }

    @Override
    public Slot save(Slot slot, UUID doctorId) {
        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setId(doctorId);

        SlotEntity slotEntity = mapSlotToEntity(slot, doctorEntity);
        SlotEntity savedEntity = this.slotJpaRepository.save(slotEntity);

        return this.slotMapper.toDomain(savedEntity);
    }

    /**
     * Retrieves all future slots (after the given date) for a specific doctor.
     *
     * @param doctorId  the ID of the doctor
     * @param startDate the minimum date (inclusive) for slots to retrieve
     * @return a list of future {@link Slot} objects for the doctor
     */
    @Override
    public List<Slot> findAllByDoctorIdAndDateGreaterThanEqual(UUID doctorId, LocalDate startDate) {
        List<SlotEntity> slotEntities = this.slotJpaRepository.findAllByDoctorIdAndDateGreaterThanEqual(doctorId, startDate);
        return slotEntities.stream()
                .map(this::mapSlotEntityToDomain)
                .toList();
    }

    @Override
    public Slot findOneByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date) {
        SlotEntity entity = this.slotJpaRepository
                .findFirstByMedicalConcerns_IdAndDate(medicalConcernId, date)
                .orElseThrow(SlotNotFoundException::new);

        return mapSlotEntityToDomain(entity);
    }

    @Override
    public List<Slot> findVisibleByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, List<String> validStatuses, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SlotEntity> slotEntities = this.slotJpaRepository.findVisibleByDoctorIdAndDateBetween(
                doctorId, startDate, endDate, validStatuses, pageable
        );
        return slotEntities.getContent().stream()
                .map(this::mapSlotEntityToDomain)
                .toList();
    }

    @Override
    public List<Slot> getByNextDateAndMedicalConcernId(LocalDate date, UUID medicalConcernId) {
        List<SlotEntity> entities = this.slotJpaRepository.findAllByDateAfterAndMedicalConcernId(medicalConcernId, date);

        Optional<LocalDate> closestNextDate = entities.stream()
                .map(SlotEntity::getDate)
                .min(Comparator.naturalOrder()); // la plus proche date future

        return closestNextDate.map(localDate -> entities.stream()
                .filter(slot -> slot.getDate().equals(localDate))
                .map(this::mapSlotEntityToDomain)
                .toList()).orElseGet(List::of);

    }

    @Override
    public List<Slot> getByPreviousDateAndMedicalConcernId(LocalDate date, UUID medicalConcernId) {
        List<SlotEntity> entities = this.slotJpaRepository.findAllByDateBeforeAndMedicalConcernId(medicalConcernId, date);

        // Trouve la date la plus récente avant `date`
        Optional<LocalDate> closestPreviousDate = entities.stream()
                .map(SlotEntity::getDate)
                .max(Comparator.naturalOrder());

        return closestPreviousDate.map(localDate -> entities.stream()
                .filter(slot -> slot.getDate().equals(localDate))
                .map(this::mapSlotEntityToDomain)
                .toList()).orElseGet(List::of);
    }


    @Override
    public List<Slot> findVisibleByDoctorIdAndDateAfter(UUID doctorId, LocalDate startDate, List<String> validStatuses, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SlotEntity> slotEntities = this.slotJpaRepository.findVisibleByDoctorIdAndDateAfter(
                doctorId, startDate, validStatuses, pageable
        );
        return slotEntities.getContent().stream()
                .map(this::mapSlotEntityToDomain)
                .toList();
    }

    @Override
    public List<Slot> findAllByDoctorIdAndDate(UUID doctorId, LocalDate date) {
        List<SlotEntity> slotEntities = this.slotJpaRepository.findAllByDoctor_IdAndDate(doctorId, date);
        return slotEntities.stream()
                .map(this::mapSlotEntityToDomain)
                .toList();
    }

    @Override
    public Slot findVisibleById(UUID id, List<String> validStatuses) throws SlotNotFoundException {
        SlotEntity entity = this.slotJpaRepository.findVisibleById(id, validStatuses)
                .orElseThrow(SlotNotFoundException::new);
        return mapSlotEntityToDomain(entity);
    }

    @Override
    public Slot update(Slot slot) {
        SlotEntity entity = this.entityManager.getReference(SlotEntity.class, slot.getId());

        entity.setStartHour(slot.getHoursRange().getStart());
        entity.setEndHour(slot.getHoursRange().getEnd());

        List<UUID> medicalConcernIds = slot.getAvailableMedicalConcerns()
                .stream()
                .map(MedicalConcern::getId)
                .toList();
        List<MedicalConcernEntity> medicalConcernEntities = this.medicalConcernJpaRepository.findAllById(medicalConcernIds);
        entity.setMedicalConcerns(medicalConcernEntities);

        SlotEntity updated = this.slotJpaRepository.save(entity);

        return mapSlotEntityToDomain(updated);
    }

    @Override
    public Slot delete(UUID slotId) {
        SlotEntity entity = this.entityManager.getReference(SlotEntity.class, slotId);
        entity.setDeletedAt(LocalDate.now());
        SlotEntity updated = this.slotJpaRepository.save(entity);
        return mapSlotEntityToDomain(updated);
    }

    private SlotEntity mapSlotToEntity(Slot slot, DoctorEntity doctorEntity) {
        List<UUID> medicalConcernIds = slot.getAvailableMedicalConcerns().stream()
                .map(MedicalConcern::getId)
                .toList();

        List<MedicalConcernEntity> medicalConcernEntities = this.medicalConcernJpaRepository.findAllById(medicalConcernIds);

        return this.slotMapper.toEntity(
                slot,
                List.of(),
                medicalConcernEntities,
                doctorEntity
        );
    }

    private Slot mapSlotEntityToDomain(SlotEntity slotEntity) {
        List<Appointment> appointments = slotEntity.getAppointments().stream()
                .map(this.appointmentFacadeMapper::mapAppointmentToDomain)
                .toList();

        List<MedicalConcern> medicalConcerns = slotEntity.getMedicalConcerns().stream()
                .map(this.medicalConcernMapper::toDomain)
                .toList();
        return this.slotMapper.toDomain(slotEntity, appointments, medicalConcerns);
    }
}
