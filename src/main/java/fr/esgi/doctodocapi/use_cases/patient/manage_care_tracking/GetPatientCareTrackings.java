package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetAppointmentOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDoctorOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackings;
import org.springframework.http.HttpStatus;

import java.util.Comparator;
import java.util.List;

public class GetPatientCareTrackings implements IGetPatientCareTrackings {
    private final CareTrackingRepository careTrackingRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final AppointmentRepository appointmentRepository;


    public GetPatientCareTrackings(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, AppointmentRepository appointmentRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.appointmentRepository = appointmentRepository;
    }

    public List<GetPatientCareTrackingResponse> process(int page, int size) {
        try {
            Patient patient = this.getPatientFromContext.get();

            List<CareTracking> caresTracking = this.careTrackingRepository.findAllOpenedByPatientId(patient.getId(), page, size);


            return caresTracking.stream().map(careTracking ->
            {
                List<Appointment> appointments = careTracking.getAppointmentsId()
                        .stream()
                        .map(appointmentRepository::getById)
                        .sorted(Comparator.comparing(Appointment::getDate).reversed())
                        .toList();

                List<GetAppointmentOfCareTrackingResponse> appointmentsResponse = appointments.stream().map(appointment ->
                        {
                            Doctor doctor = appointment.getDoctor();
                            GetDoctorOfCareTrackingResponse doctorResponse = new GetDoctorOfCareTrackingResponse(
                                    doctor.getId(),
                                    doctor.getPersonalInformations().getFirstName(),
                                    doctor.getPersonalInformations().getLastName(),
                                    doctor.getProfessionalInformations().getSpeciality().getName(),
                                    doctor.getPersonalInformations().getProfilePictureUrl()
                            );

                            return new GetAppointmentOfCareTrackingResponse(
                                    appointment.getId(),
                                    doctorResponse,
                                    appointment.getDate(),
                                    appointment.getHoursRange().getStart()
                            );

                        }


                ).toList();

                return new GetPatientCareTrackingResponse(
                        careTracking.getId(),
                        careTracking.getCaseName(),
                        careTracking.getDescription(),
                        appointmentsResponse
                );

            }).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
