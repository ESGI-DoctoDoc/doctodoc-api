package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.*;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.appointment.exceptions.AppointmentNotFound;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the AppointmentRepository interface.
 * This service provides methods to manage appointments, including retrieving and saving appointment data.
 * It interacts with various JPA repositories to access and manipulate appointment-related data in the database.
 */
@Service
public class AppointmentRepositoryImpl implements AppointmentRepository {
    /**
     * Repository for accessing appointment data in the database.
     */
    private final AppointmentJpaRepository appointmentJpaRepository;

    /**
     * Repository for accessing slot data in the database.
     */
    private final SlotJpaRepository slotJpaRepository;

    /**
     * Repository for accessing patient data in the database.
     */
    private final PatientJpaRepository patientJpaRepository;

    /**
     * Repository for accessing doctor data in the database.
     */
    private final DoctorJpaRepository doctorJpaRepository;

    /**
     * Repository for accessing medical concern data in the database.
     */
    private final MedicalConcernJpaRepository medicalConcernJpaRepository;

    /**
     * Mapper for converting between appointment domain objects and entities.
     */
    private final AppointmentMapper appointmentMapper;

    /**
     * Facade mapper for converting between appointment entities and domain objects.
     */
    private final AppointmentFacadeMapper appointmentFacadeMapper;

    /**
     * Constructs an AppointmentRepositoryImpl with the required repositories and mappers.
     *
     * @param appointmentJpaRepository    Repository for appointment data access
     * @param slotJpaRepository           Repository for slot data access
     * @param patientJpaRepository        Repository for patient data access
     * @param doctorJpaRepository         Repository for doctor data access
     * @param medicalConcernJpaRepository Repository for medical concern data access
     * @param appointmentMapper           Mapper for appointment domain objects and entities
     * @param appointmentFacadeMapper     Facade mapper for appointment entities and domain objects
     */
    public AppointmentRepositoryImpl(AppointmentJpaRepository appointmentJpaRepository,
                                     SlotJpaRepository slotJpaRepository,
                                     PatientJpaRepository patientJpaRepository,
                                     DoctorJpaRepository doctorJpaRepository,
                                     MedicalConcernJpaRepository medicalConcernJpaRepository,
                                     AppointmentMapper appointmentMapper,
                                     AppointmentFacadeMapper appointmentFacadeMapper
    ) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.slotJpaRepository = slotJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.medicalConcernJpaRepository = medicalConcernJpaRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentFacadeMapper = appointmentFacadeMapper;
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
        List<AppointmentEntity> appointmentsFoundBySlot = this.appointmentJpaRepository.findAllBySlot_Id(slotId);
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
    public void save(Appointment appointment) {
        SlotEntity slotEntity = this.slotJpaRepository.findById(appointment.getSlot().getId()).orElseThrow(SlotNotFoundException::new);
        PatientEntity patientEntity = this.patientJpaRepository.findById(appointment.getPatient().getId()).orElseThrow(PatientNotFoundException::new);
        DoctorEntity doctorEntity = this.doctorJpaRepository.findById(appointment.getDoctor().getId()).orElseThrow(DoctorNotFoundException::new);
        MedicalConcernEntity medicalConcernEntity = this.medicalConcernJpaRepository.findById(appointment.getDoctor().getId()).orElseThrow(MedicalConcernNotFoundException::new);

        AppointmentEntity entity = this.appointmentMapper.toEntity(appointment, slotEntity, patientEntity, doctorEntity, medicalConcernEntity);
        this.appointmentJpaRepository.save(entity);
    }
}
