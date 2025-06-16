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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    /**
     * Constructs a SlotRepositoryImpl with the required repositories and mappers.
     *
     * @param slotJpaRepository       Repository for slot data access
     * @param slotMapper              Mapper for slot domain objects and entities
     * @param appointmentFacadeMapper Facade mapper for appointment entities and domain objects
     * @param medicalConcernMapper    Mapper for medical concern domain objects and entities
     */
    public SlotRepositoryImpl(SlotJpaRepository slotJpaRepository, SlotMapper slotMapper, AppointmentFacadeMapper appointmentFacadeMapper, MedicalConcernMapper medicalConcernMapper, MedicalConcernJpaRepository medicalConcernJpaRepository) {
        this.slotJpaRepository = slotJpaRepository;
        this.slotMapper = slotMapper;
        this.appointmentFacadeMapper = appointmentFacadeMapper;
        this.medicalConcernMapper = medicalConcernMapper;
        this.medicalConcernJpaRepository = medicalConcernJpaRepository;
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
        List<SlotEntity> slotsFoundByMedicalConcern = this.slotJpaRepository.findAllByMedicalConcerns_IdAndDate(medicalConcernId, date);

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

    /**
     * Retrieves all future slots (after the given date) for a specific doctor.
     *
     * @param doctorId  the ID of the doctor
     * @param startDate the minimum date (inclusive) for slots to retrieve
     * @return a list of future {@link Slot} objects for the doctor
     */
    @Override
    public List<Slot> findAllByDoctorIdAndDateAfter(UUID doctorId, LocalDate startDate) {
        List<SlotEntity> slotEntities = this.slotJpaRepository.findAllByDoctor_IdAndDateAfter(doctorId, startDate);
        return slotEntities.stream()
                .map(this::mapSlotEntityToDomain)
                .toList();
    }

    /**
     * Retrieves paginated slots for a specific doctor within a date range.
     *
     * @param doctorId  the ID of the doctor
     * @param startDate start of the date range (inclusive)
     * @param endDate   end of the date range (inclusive)
     * @param page      page number (zero-based)
     * @param size      number of elements per page
     * @return a paginated list of {@link Slot} objects
     */
    @Override
    public List<Slot> findAllByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SlotEntity> slotEntities = this.slotJpaRepository.findAllByDoctor_IdAndDateBetween(doctorId, startDate, endDate, pageable);
        return slotEntities.getContent().stream()
                .map(this::mapSlotEntityToDomain)
                .toList();
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
