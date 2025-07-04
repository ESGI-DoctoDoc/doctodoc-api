package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingDetailedResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackingDetailed;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;

public class GetPatientCareTrackingDetailed implements IGetPatientCareTrackingDetailed {
    private final CareTrackingRepository careTrackingRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final GetPatientCareTrackingMapper getPatientCareTrackingMapper;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;


    public GetPatientCareTrackingDetailed(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, GetPatientCareTrackingMapper getPatientCareTrackingMapper, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.getPatientCareTrackingMapper = getPatientCareTrackingMapper;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public GetPatientCareTrackingDetailedResponse process(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(id, patient);
            List<Doctor> doctors = getDoctors(careTracking);
            List<Appointment> appointments = careTracking.getAppointmentsId().stream().map(appointmentRepository::getById).toList();
            List<CareTrackingDocument> documents = careTracking
                    .getDocuments()
                    .stream()
                    .sorted(comparing((CareTrackingDocument doc) -> doc.getDocument().getUploadedAt()).reversed())
                    .toList();

            return this.getPatientCareTrackingMapper.toDto(careTracking, doctors, appointments, documents);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }

    private List<Doctor> getDoctors(CareTracking careTracking) {
        List<Doctor> doctors = new ArrayList<>(careTracking.getDoctors().stream().map(doctorRepository::getById).toList());
        Doctor creator = this.doctorRepository.getById(careTracking.getCreatorId());
        doctors.add(creator);
        return doctors;
    }
}
