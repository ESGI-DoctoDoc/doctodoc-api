package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AppointmentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PreAppointmentAnswersJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.QuestionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.PreAppointmentAnswersMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFound;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the AppointmentRepository interface.
 * This service provides methods to manage appointments, including retrieving and saving appointment data.
 * It interacts with various JPA repositories to access and manipulate appointment-related data in the database.
 */
@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {
    /**
     * Repository for accessing appointment data in the database.
     */
    private final AppointmentJpaRepository appointmentJpaRepository;

    /**
     * Mapper for converting between appointment domain objects and entities.
     */
    private final AppointmentMapper appointmentMapper;

    /**
     * Facade mapper for converting between appointment entities and domain objects.
     */
    private final AppointmentFacadeMapper appointmentFacadeMapper;

    private final PreAppointmentAnswersMapper preAppointmentAnswersMapper;
    private final PreAppointmentAnswersJpaRepository preAppointmentAnswersJpaRepository;
    private final QuestionJpaRepository questionJpaRepository;

    private final EntityManager entityManager;


    /**
     * Constructs an AppointmentRepositoryImpl with the required repositories and mappers.
     *
     * @param appointmentJpaRepository    Repository for appointment data access
     * @param appointmentMapper           Mapper for appointment domain objects and entities
     * @param appointmentFacadeMapper     Facade mapper for appointment entities and domain objects
     */
    public AppointmentRepositoryImpl(
            EntityManager entityManager,
            AppointmentJpaRepository appointmentJpaRepository,
                                     AppointmentMapper appointmentMapper,
                                     AppointmentFacadeMapper appointmentFacadeMapper,
                                     PreAppointmentAnswersMapper preAppointmentAnswersMapper,
                                     PreAppointmentAnswersJpaRepository preAppointmentAnswersJpaRepository,
                                     QuestionJpaRepository questionJpaRepository

    ) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentFacadeMapper = appointmentFacadeMapper;
        this.preAppointmentAnswersMapper = preAppointmentAnswersMapper;
        this.preAppointmentAnswersJpaRepository = preAppointmentAnswersJpaRepository;
        this.questionJpaRepository = questionJpaRepository;
        this.entityManager = entityManager;
    }

    /**
     * Retrieves an appointment by its unique identifier.
     *
     * @param id The unique identifier of the appointment to retrieve
     * @return The appointment with the specified ID
     * @throws AppointmentNotFound If no appointment with the specified ID exists
     */
    @Override
    public Appointment getById(UUID id) throws AppointmentNotFound {
        AppointmentEntity appointmentEntity = this.appointmentJpaRepository.findById(id).orElseThrow(AppointmentNotFound::new);
        return appointmentFacadeMapper.mapAppointmentToDomain(appointmentEntity);
    }

    /**
     * Retrieves all appointments associated with a specific slot.
     *
     * @param slotId The unique identifier of the slot
     * @return A list of appointments associated with the specified slot
     */
    @Override
    public List<Appointment> getAppointmentsBySlot(UUID slotId) {
        List<AppointmentEntity> appointmentsFoundBySlot = this.appointmentJpaRepository.findAllBySlot_IdAndDeletedAtIsNull(slotId);
        return appointmentsFoundBySlot.stream().map(appointmentFacadeMapper::mapAppointmentToDomain).toList();
    }

    /**
     * Saves an appointment to the database.
     * This method retrieves the necessary related entities (slot, patient, doctor, medical concern)
     * before creating and saving the appointment entity.
     *
     * @param appointment The appointment to save
     * @throws SlotNotFoundException If the slot associated with the appointment does not exist
     * @throws PatientNotFoundException If the patient associated with the appointment does not exist
     * @throws DoctorNotFoundException If the doctor associated with the appointment does not exist
     * @throws MedicalConcernNotFoundException If the medical concern associated with the appointment does not exist
     */
    @Override
    public UUID save(Appointment appointment) {
        SlotEntity slotEntity = this.entityManager.getReference(SlotEntity.class, appointment.getSlot().getId());
        PatientEntity patientEntity = this.entityManager.getReference(PatientEntity.class, appointment.getPatient().getId());
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, appointment.getDoctor().getId());
        MedicalConcernEntity medicalConcernEntity = this.entityManager.getReference(MedicalConcernEntity.class, appointment.getDoctor().getId());

        AppointmentEntity entity = this.appointmentMapper.toEntity(appointment, slotEntity, patientEntity, doctorEntity, medicalConcernEntity);
        AppointmentEntity entityCreated = this.appointmentJpaRepository.save(entity);

        // save answers
        List<PreAppointmentAnswers> answers = appointment.getPreAppointmentAnswers();
        List<PreAppointmentAnswersEntity> answersEntities = answers.stream().map(preAppointmentAnswers -> {
            QuestionEntity questionEntity = this.questionJpaRepository.findById(preAppointmentAnswers.getQuestion().getId()).orElseThrow(QuestionNotFoundException::new);
            return this.preAppointmentAnswersMapper.toEntity(preAppointmentAnswers.getResponse(), entity, questionEntity);
        }).toList();
        this.preAppointmentAnswersJpaRepository.saveAll(answersEntities);

        return entityCreated.getId();
    }

    @Override
    public void confirm(Appointment appointment) throws SlotNotFoundException, PatientNotFoundException, DoctorNotFoundException, MedicalConcernNotFoundException, QuestionNotFoundException {
        AppointmentEntity appointmentEntity = this.appointmentJpaRepository.findById(appointment.getId()).orElseThrow(AppointmentNotFound::new);
        appointmentEntity.setStatus(appointment.getStatus().name());
        appointmentEntity.setLockedAt(appointment.getLockedAt());
        this.appointmentJpaRepository.save(appointmentEntity);
    }

    @Override
    public void delete(UUID id) {
        AppointmentEntity entity = this.appointmentJpaRepository.findById(id).orElseThrow(AppointmentNotFound::new);
        entity.setDeletedAt(LocalDateTime.now());
        this.appointmentJpaRepository.save(entity);
    }

    @Override
    public List<Appointment> getAllByUserAndStatusOrderByDateAsc(User user, AppointmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointments = this.appointmentJpaRepository.findAllByPatient_User_IdAndStatusOrderByDateAsc(user.getId(), status.name(), pageable);

        return appointments.getContent().stream().map(appointmentFacadeMapper::mapAppointmentToDomain).toList();
    }

    @Override
    public List<Appointment> getAllByUserAndStatusOrderByDateDesc(User user, AppointmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointments = this.appointmentJpaRepository.findAllByPatient_User_IdAndStatusOrderByDateDesc(user.getId(), status.name(), pageable);

        return appointments.getContent().stream().map(appointmentFacadeMapper::mapAppointmentToDomain).toList();
    }

    @Override
    public Optional<Appointment> getMostRecentUpcomingAppointment(User user) {
        Optional<AppointmentEntity> appointmentEntity = this.appointmentJpaRepository.findFirstByPatient_User_IdAndStatusAndDateAfterOrderByDateAsc(user.getId(), AppointmentStatus.CONFIRMED.name(), LocalDate.now());
        return appointmentEntity.map(this.appointmentFacadeMapper::mapAppointmentToDomain);
    }
}
