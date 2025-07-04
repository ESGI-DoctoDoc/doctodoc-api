package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AppointmentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.PreAppointmentAnswersJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.QuestionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.PatientMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.PreAppointmentAnswersMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFoundException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
     * Mapper for converting between patient domain objects and entities.
     */
    private final PatientMapper patientMapper;

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
     * @param appointmentJpaRepository Repository for appointment data access
     * @param appointmentMapper        Mapper for appointment domain objects and entities
     * @param appointmentFacadeMapper  Facade mapper for appointment entities and domain objects
     */
    public AppointmentRepositoryImpl(
            EntityManager entityManager,
            AppointmentJpaRepository appointmentJpaRepository,
            AppointmentMapper appointmentMapper, PatientMapper patientMapper,
            AppointmentFacadeMapper appointmentFacadeMapper,
            PreAppointmentAnswersMapper preAppointmentAnswersMapper,
            PreAppointmentAnswersJpaRepository preAppointmentAnswersJpaRepository,
            QuestionJpaRepository questionJpaRepository

    ) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.appointmentMapper = appointmentMapper;
        this.patientMapper = patientMapper;
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
     * @throws AppointmentNotFoundException If no appointment with the specified ID exists
     */
    @Override
    public Appointment getById(UUID id) throws AppointmentNotFoundException {
        AppointmentEntity appointmentEntity = this.appointmentJpaRepository.findById(id).orElseThrow(AppointmentNotFoundException::new);
        return appointmentFacadeMapper.mapAppointmentToDomain(appointmentEntity);
    }


    @Override
    public Appointment getByIdAndPatientId(UUID id, UUID patientId) throws AppointmentNotFoundException {
        AppointmentEntity appointmentEntity = this.appointmentJpaRepository.findByIdAndPatient_Id(id, patientId).orElseThrow(AppointmentNotFoundException::new);
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
     * @throws SlotNotFoundException           If the slot associated with the appointment does not exist
     * @throws PatientNotFoundException        If the patient associated with the appointment does not exist
     * @throws DoctorNotFoundException         If the doctor associated with the appointment does not exist
     * @throws MedicalConcernNotFoundException If the medical concern associated with the appointment does not exist
     */
    @Override
    public UUID save(Appointment appointment) {
        SlotEntity slotEntity = this.entityManager.getReference(SlotEntity.class, appointment.getSlot().getId());
        PatientEntity patientEntity = this.entityManager.getReference(PatientEntity.class, appointment.getPatient().getId());
        DoctorEntity doctorEntity = this.entityManager.getReference(DoctorEntity.class, appointment.getDoctor().getId());
        MedicalConcernEntity medicalConcernEntity = this.entityManager.getReference(MedicalConcernEntity.class, appointment.getMedicalConcern().getId());
        CareTrackingEntity careTrackingEntity = appointment.getCareTrackingId() != null ? this.entityManager.getReference(CareTrackingEntity.class, appointment.getCareTrackingId()) : null;

        AppointmentEntity entity = this.appointmentMapper.toEntity(appointment, slotEntity, patientEntity, doctorEntity, medicalConcernEntity, careTrackingEntity);
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
    public void cancel(Appointment appointment) {
        AppointmentEntity appointmentEntity = this.appointmentJpaRepository.findById(appointment.getId()).orElseThrow(AppointmentNotFoundException::new);
        appointmentEntity.setStatus(appointment.getStatus().getValue());
        this.appointmentJpaRepository.save(appointmentEntity);
    }

    @Override
    public void confirm(Appointment appointment) throws SlotNotFoundException, PatientNotFoundException, DoctorNotFoundException, MedicalConcernNotFoundException, QuestionNotFoundException {
        AppointmentEntity appointmentEntity = this.appointmentJpaRepository.findById(appointment.getId()).orElseThrow(AppointmentNotFoundException::new);
        appointmentEntity.setStatus(appointment.getStatus().getValue());
        appointmentEntity.setLockedAt(appointment.getLockedAt());
        this.appointmentJpaRepository.save(appointmentEntity);
    }

    @Override
    public void delete(Appointment appointment) {
        AppointmentEntity entity = this.appointmentJpaRepository.findById(appointment.getId()).orElseThrow(AppointmentNotFoundException::new);
        entity.setStatus(appointment.getStatus().getValue());
        LocalDateTime now = LocalDateTime.now();
        entity.setDeletedAt(now);
        List<PreAppointmentAnswersEntity> answers = entity.getAppointmentQuestions();
        answers.forEach(answer -> {
            answer.setDeletedAt(now);
            this.preAppointmentAnswersJpaRepository.save(answer);
        });
        this.appointmentJpaRepository.save(entity);
    }

    @Override
    public List<Appointment> getAllByUserAndStatusOrderByDateAsc(User user, AppointmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointments = this.appointmentJpaRepository.findAllByPatient_User_IdAndStatusOrderByDateAsc(user.getId(), status.getValue(), pageable);

        return appointments.getContent().stream().map(appointmentFacadeMapper::mapAppointmentToDomain).toList();
    }

    @Override
    public List<Appointment> getAllByUserAndStatusOrderByDateDesc(User user, AppointmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointments = this.appointmentJpaRepository.findAllByPatient_User_IdAndStatusOrderByDateDesc(user.getId(), status.getValue(), pageable);

        return appointments.getContent().stream().map(appointmentFacadeMapper::mapAppointmentToDomain).toList();
    }

    @Override
    public Optional<Appointment> getMostRecentUpcomingAppointment(User user) {
        Optional<AppointmentEntity> appointmentEntity = this.appointmentJpaRepository.findFirstByPatient_User_IdAndStatusAndDateAfterOrderByDateAsc(user.getId(), AppointmentStatus.CONFIRMED.getValue(), LocalDate.now());
        return appointmentEntity.map(this.appointmentFacadeMapper::mapAppointmentToDomain);
    }

    @Override
    public List<Patient> getDistinctPatientsByDoctorId(UUID doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointmentEntities = this.appointmentJpaRepository.findAllByDoctor_Id(doctorId, pageable);

        Set<PatientEntity> patientEntities = appointmentEntities.getContent().stream()
                .map(AppointmentEntity::getPatient)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return patientEntities.stream()
                .map(patientMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsPatientByDoctorAndPatientId(UUID doctorId, UUID patientId) {
        return this.appointmentJpaRepository.existsByDoctor_IdAndPatient_Id(doctorId, patientId);
    }

    @Override
    public List<Appointment> findAllWithPaginationForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointments = this.appointmentJpaRepository.findAllVisibleForAdmin(pageable);
        return appointments.getContent().stream()
                .map(appointmentFacadeMapper::mapAppointmentToDomain)
                .toList();
    }

    @Override
    public int countAppointmentsByDoctorId(UUID doctorId) {
        return this.appointmentJpaRepository.countByDoctor_IdAndDateGreaterThanEqual(doctorId, LocalDate.now());
    }

    @Override
    public int countDistinctPatientsByDoctorId(UUID doctorId) {
        return this.appointmentJpaRepository.countDistinctPatientsByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> findVisibleAppointmentsByDoctorId(UUID doctorId, List<String> validStatuses, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointments = this.appointmentJpaRepository.findVisibleAppointmentsByDoctorId(
                doctorId, validStatuses, pageable
        );
        return appointments.getContent().stream()
                .map(appointmentFacadeMapper::mapAppointmentToDomain)
                .toList();
    }

    @Override
    public List<Appointment> findVisibleAppointmentsByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, List<String> validStatuses, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentEntity> appointments = this.appointmentJpaRepository.findVisibleAppointmentsByDoctorIdAndDateBetween(
                doctorId, startDate, endDate, validStatuses, pageable
        );
        return appointments.getContent().stream()
                .map(appointmentFacadeMapper::mapAppointmentToDomain)
                .toList();
    }

    @Override
    public Appointment getVisibleById(UUID id, List<String> validStatuses) throws AppointmentNotFoundException {
        AppointmentEntity entity = this.appointmentJpaRepository.findVisibleById(id, validStatuses)
                .orElseThrow(AppointmentNotFoundException::new);
        return appointmentFacadeMapper.mapAppointmentToDomain(entity);
    }
}
