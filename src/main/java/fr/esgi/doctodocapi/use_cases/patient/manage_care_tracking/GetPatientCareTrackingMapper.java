package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.*;
import fr.esgi.doctodocapi.use_cases.patient.utils.GetDoctorProfileUrl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPatientCareTrackingMapper {

    private final GetDoctorProfileUrl getDoctorProfileUrl;

    public GetPatientCareTrackingMapper(GetDoctorProfileUrl getDoctorProfileUrl) {
        this.getDoctorProfileUrl = getDoctorProfileUrl;
    }

    public GetPatientCareTrackingDetailedResponse toDto(CareTracking careTracking, List<Doctor> doctors, List<Appointment> appointments, List<Document> documents) {
        List<GetDoctorOfCareTrackingResponse> doctorsResponse = getGetDoctorOfCareTrackingResponses(doctors);
        List<GetAppointmentOfCareTrackingResponse> appointmentsResponse = getAppointmentsResponse(appointments);
        List<GetDocumentsOfCareTrackingResponse> documentsResponses = getDocumentsResponses(documents);


        return new GetPatientCareTrackingDetailedResponse(
                careTracking.getId(),
                careTracking.getCaseName(),
                careTracking.getDescription(),
                careTracking.getCreatedAt(),
                careTracking.getClosedAt(),
                doctorsResponse,
                appointmentsResponse,
                documentsResponses
        );

    }

    public GetPatientCareTrackingResponse toDto(CareTracking careTracking, List<Appointment> appointments) {
        List<GetAppointmentOfCareTrackingResponse> appointmentsResponse = getAppointmentsResponse(appointments);

        return new GetPatientCareTrackingResponse(
                careTracking.getId(),
                careTracking.getCaseName(),
                careTracking.getDescription(),
                careTracking.getCreatedAt(),
                careTracking.getClosedAt(),
                appointmentsResponse
        );

    }

    private List<GetDocumentsOfCareTrackingResponse> getDocumentsResponses(List<Document> documents) {
        return documents.stream().map(document ->
                new GetDocumentsOfCareTrackingResponse(document.getId(), document.getName(), document.getType().getValue(), document.getPath())
        ).toList();
    }

    private List<GetAppointmentOfCareTrackingResponse> getAppointmentsResponse(List<Appointment> appointments) {
        return appointments.stream().map(appointment ->
        {
            Doctor doctor = appointment.getDoctor();
            String profileUrl = this.getDoctorProfileUrl.getUrl(doctor.getPersonalInformations().getProfilePictureUrl());

            GetDoctorOfCareTrackingResponse doctorResponse = new GetDoctorOfCareTrackingResponse(
                    doctor.getId(),
                    doctor.getPersonalInformations().getFirstName(),
                    doctor.getPersonalInformations().getLastName(),
                    doctor.getProfessionalInformations().getSpeciality().getName(),
                    profileUrl
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
        {
            String profileUrl = this.getDoctorProfileUrl.getUrl(doctor.getPersonalInformations().getProfilePictureUrl());

            return new GetDoctorOfCareTrackingResponse(doctor.getId(),
                    doctor.getPersonalInformations().getLastName(),
                    doctor.getPersonalInformations().getFirstName(),
                    doctor.getProfessionalInformations().getSpeciality().getName(),
                    profileUrl
            );
        }).toList();
    }
}
