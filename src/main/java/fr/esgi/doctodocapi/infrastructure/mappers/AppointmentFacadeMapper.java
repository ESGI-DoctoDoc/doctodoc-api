package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

@Service
public class AppointmentFacadeMapper {
    private final AppointmentMapper appointmentMapper;
    private final SlotMapper slotMapper;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final MedicalConcernMapper medicalConcernMapper;

    public AppointmentFacadeMapper(AppointmentMapper appointmentMapper, SlotMapper slotMapper, PatientMapper patientMapper, DoctorMapper doctorMapper, MedicalConcernMapper medicalConcernMapper) {
        this.appointmentMapper = appointmentMapper;
        this.slotMapper = slotMapper;
        this.patientMapper = patientMapper;
        this.doctorMapper = doctorMapper;
        this.medicalConcernMapper = medicalConcernMapper;
    }

    public Appointment mapAppointmentToDomain(AppointmentEntity appointmentEntity) {
        Slot slot = this.slotMapper.toDomain(appointmentEntity.getSlot());
        Patient patient = this.patientMapper.toDomain(appointmentEntity.getPatient());
        Doctor doctor = this.doctorMapper.toDomain(appointmentEntity.getDoctor());
        MedicalConcern medicalConcern = this.medicalConcernMapper.toDomain(appointmentEntity.getMedicalConcern());

        return this.appointmentMapper.toDomain(appointmentEntity, slot, patient, doctor, medicalConcern);
    }
}
