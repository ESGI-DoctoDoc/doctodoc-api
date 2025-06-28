package fr.esgi.doctodocapi.use_cases.patient.make_appointment;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.personal_information.DoctorPersonnalInformations;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentResponse;
import org.springframework.stereotype.Service;

@Service
public class AppointmentPresentationMapper {

    public GetAppointmentResponse toDto(Appointment appointment) {
        DoctorPersonnalInformations doctorPersonnalInformations = appointment.getDoctor().getPersonalInformations();
        GetAppointmentDoctorResponse getDoctorAppointmentResponse = new GetAppointmentDoctorResponse(
                appointment.getDoctor().getId(),
                doctorPersonnalInformations.getFirstName(),
                doctorPersonnalInformations.getLastName(),
                appointment.getDoctor().getProfessionalInformations().getSpeciality(),
                doctorPersonnalInformations.getProfilePictureUrl()
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
