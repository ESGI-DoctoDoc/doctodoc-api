package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.SlotJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.MedicalConcernMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.SlotMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
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
     * Constructs a SlotRepositoryImpl with the required repositories and mappers.
     *
     * @param slotJpaRepository       Repository for slot data access
     * @param slotMapper              Mapper for slot domain objects and entities
     * @param appointmentFacadeMapper Facade mapper for appointment entities and domain objects
     * @param medicalConcernMapper    Mapper for medical concern domain objects and entities
     */
    public SlotRepositoryImpl(SlotJpaRepository slotJpaRepository, SlotMapper slotMapper, AppointmentFacadeMapper appointmentFacadeMapper, MedicalConcernMapper medicalConcernMapper) {
        this.slotJpaRepository = slotJpaRepository;
        this.slotMapper = slotMapper;
        this.appointmentFacadeMapper = appointmentFacadeMapper;
        this.medicalConcernMapper = medicalConcernMapper;
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

        return slotsFoundByMedicalConcern.stream().map(slotEntity -> {
            List<Appointment> appointments = slotEntity.getAppointments().stream().map(this.appointmentFacadeMapper::mapAppointmentToDomain).toList();
            List<MedicalConcern> medicalConcerns = slotEntity.getMedicalConcerns().stream().map(this.medicalConcernMapper::toDomain).toList();
            return this.slotMapper.toDomain(slotEntity, appointments, medicalConcerns);
        }).toList();
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
        List<Appointment> appointments = entity.getAppointments().stream().map(this.appointmentFacadeMapper::mapAppointmentToDomain).toList();
        List<MedicalConcern> medicalConcerns = entity.getMedicalConcerns().stream().map(this.medicalConcernMapper::toDomain).toList();

        return this.slotMapper.toDomain(entity, appointments, medicalConcerns);
    }


}
