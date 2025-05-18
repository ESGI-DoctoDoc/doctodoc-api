package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.*;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentNotFound;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final AppointmentJpaRepository appointmentJpaRepository;
    private final SlotJpaRepository slotJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final MedicalConcernJpaRepository medicalConcernJpaRepository;

    private final AppointmentMapper appointmentMapper;
    private final AppointmentFacadeMapper appointmentFacadeMapper;

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

    @Override
    public Appointment getById(UUID id) throws AppointmentNotFound {
        AppointmentEntity appointmentEntity = this.appointmentJpaRepository.findById(id).orElseThrow(AppointmentNotFound::new);
        return appointmentFacadeMapper.mapAppointmentToDomain(appointmentEntity);
    }

    @Override
    public List<Appointment> getAppointmentsBySlot(UUID slotId) {
        List<AppointmentEntity> appointmentsFoundBySlot = this.appointmentJpaRepository.findAllBySlot_Id(slotId);
        return appointmentsFoundBySlot.stream().map(appointmentFacadeMapper::mapAppointmentToDomain).toList();
    }

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
