package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetAppointmentOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDoctorOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentsOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetPatientCareTrackingDetailedResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPatientCareTrackingDetailedMapper {

    public GetPatientCareTrackingDetailedResponse toDto(CareTracking careTracking, List<Doctor> doctors, List<Appointment> appointments, List<Document> documents) {
        List<GetDoctorOfCareTrackingResponse> doctorsResponse = getGetDoctorOfCareTrackingResponses(doctors);
        List<GetAppointmentOfCareTrackingResponse> appointmentsResponse = getAppointmentsResponse(appointments);
        List<GetDocumentsOfCareTrackingResponse> documentsResponses = getDocumentsResponses(documents);


        return new GetPatientCareTrackingDetailedResponse(
                careTracking.getId(),
                careTracking.getCaseName(),
                careTracking.getDescription(),
                doctorsResponse,
                appointmentsResponse,
                documentsResponses
        );

    }

    private List<GetDocumentsOfCareTrackingResponse> getDocumentsResponses(List<Document> documents) {
        return documents.stream().map(document ->
                new GetDocumentsOfCareTrackingResponse(document.getId())
        ).toList();
    }

    private List<GetAppointmentOfCareTrackingResponse> getAppointmentsResponse(List<Appointment> appointments) {
        return appointments.stream().map(appointment ->
        {
            Doctor doctor = appointment.getDoctor();
            GetDoctorOfCareTrackingResponse doctorResponse = new GetDoctorOfCareTrackingResponse(
                    doctor.getId(),
                    doctor.getPersonalInformations().getFirstName(),
                    doctor.getPersonalInformations().getLastName(),
                    doctor.getProfessionalInformations().getSpeciality(),
                    doctor.getPersonalInformations().getProfilePictureUrl()
            );

            return new GetAppointmentOfCareTrackingResponse(
                    appointment.getId(),
                    doctorResponse,
                    appointment.getDate(),
                    appointment.getHoursRange().getStart()
            );

        }).toList();
    }

    private List<GetDoctorOfCareTrackingResponse> getGetDoctorOfCareTrackingResponses(List<Doctor> doctors) {
        return doctors.stream().map(doctor ->
                new GetDoctorOfCareTrackingResponse(doctor.getId(),
                        doctor.getPersonalInformations().getLastName(),
                        doctor.getPersonalInformations().getFirstName(),
                        doctor.getProfessionalInformations().getSpeciality(),
                        doctor.getPersonalInformations().getProfilePictureUrl()

                )).toList();
    }
}
