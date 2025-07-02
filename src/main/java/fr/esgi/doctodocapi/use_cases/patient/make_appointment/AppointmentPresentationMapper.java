package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.patient.utils.GetDoctorProfileUrl;
import org.springframework.stereotype.Service;

@Service
public class AppointmentPresentationMapper {

    private final GetDoctorProfileUrl getDoctorProfileUrl;

    public AppointmentPresentationMapper(GetDoctorProfileUrl getDoctorProfileUrl) {
        this.getDoctorProfileUrl = getDoctorProfileUrl;
    }

    public GetAppointmentResponse toDto(Appointment appointment) {
        DoctorPersonnalInformations doctorPersonnalInformations = appointment.getDoctor().getPersonalInformations();

        String profileUrl = this.getDoctorProfileUrl.getUrl(appointment.getDoctor().getPersonalInformations().getProfilePictureUrl());

        GetAppointmentDoctorResponse getDoctorAppointmentResponse = new GetAppointmentDoctorResponse(
                appointment.getDoctor().getId(),
                doctorPersonnalInformations.getFirstName(),
                doctorPersonnalInformations.getLastName(),
                appointment.getDoctor().getProfessionalInformations().getSpeciality().getName(),
                profileUrl
        );

        return new GetAppointmentResponse(
                appointment.getId(),
                appointment.getDate(),
                appointment.getHoursRange().getStart(),
                appointment.getHoursRange().getEnd(),
                appointment.getDoctor().getConsultationInformations().getAddress(),
                getDoctorAppointmentResponse
        );

    }
}
