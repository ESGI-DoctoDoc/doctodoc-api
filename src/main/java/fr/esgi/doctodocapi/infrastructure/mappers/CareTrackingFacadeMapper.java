package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareTrackingFacadeMapper {
    private final CareTrackingMapper careTrackingMapper;
    private final DoctorFacadeMapper doctorFacadeMapper;
    private final PatientMapper patientMapper;
    private final CareTrackingTraceMapper careTrackingTraceMapper;
    private final AppointmentFacadeMapper appointmentFacadeMapper;

    public CareTrackingFacadeMapper(CareTrackingMapper careTrackingMapper, DoctorFacadeMapper doctorFacadeMapper, PatientMapper patientMapper, CareTrackingTraceMapper careTrackingTraceMapper, AppointmentFacadeMapper appointmentFacadeMapper) {
        this.careTrackingMapper = careTrackingMapper;
        this.doctorFacadeMapper = doctorFacadeMapper;
        this.patientMapper = patientMapper;
        this.careTrackingTraceMapper = careTrackingTraceMapper;
        this.appointmentFacadeMapper = appointmentFacadeMapper;
    }

    public CareTracking mapCareTrackingToDomain(CareTrackingEntity entity) {
        Doctor doctor = this.doctorFacadeMapper.mapDoctorToDomain(entity.getCreator());
        Patient patient = this.patientMapper.toDomain(entity.getPatient());

        List<Appointment> appointments = entity.getAppointments()
                .stream()
                .map(appointmentFacadeMapper::mapAppointmentToDomain)
                .toList();

        List<CareTrackingTrace> traces = entity.getCareTrackingTraces()
                .stream()
                .map(traceEntity -> careTrackingTraceMapper.toDomain(traceEntity, doctorFacadeMapper.mapDoctorToDomain(traceEntity.getConsultedBy())))
                .toList();

        return this.careTrackingMapper.toDomain(entity, doctor, patient, appointments, traces);
    }
}
