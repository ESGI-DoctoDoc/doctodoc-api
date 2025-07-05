package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackings;
import org.springframework.http.HttpStatus;

import java.util.Comparator;
import java.util.List;

public class GetPatientCareTrackings implements IGetPatientCareTrackings {
    private final CareTrackingRepository careTrackingRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final AppointmentRepository appointmentRepository;
    private final GetPatientCareTrackingMapper getPatientCareTrackingMapper;


    public GetPatientCareTrackings(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, AppointmentRepository appointmentRepository, GetPatientCareTrackingMapper getPatientCareTrackingMapper) {
        this.careTrackingRepository = careTrackingRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.appointmentRepository = appointmentRepository;
        this.getPatientCareTrackingMapper = getPatientCareTrackingMapper;
    }

    public List<GetPatientCareTrackingResponse> process(int page, int size) {
        try {
            Patient patient = this.getPatientFromContext.get();

            List<CareTracking> caresTracking = this.careTrackingRepository.findAllByPatientId(patient.getId(), page, size);

            return caresTracking.stream().map(careTracking -> {
                List<Appointment> appointments = careTracking.getAppointmentsId()
                        .stream()
                        .map(appointmentRepository::getById)
                        .sorted(Comparator.comparing(Appointment::getDate).reversed())
                        .toList();

                return this.getPatientCareTrackingMapper.toDto(careTracking, appointments);
            }).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
