package fr.esgi.doctodocapi.use_cases.patient.mappers;

import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentAddressResponse;
import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentDetailedResponse;
import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentDoctorResponse;
import fr.esgi.doctodocapi.dtos.responses.appointment_response.GetAppointmentPatientResponse;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import org.springframework.stereotype.Service;

@Service
public class AppointmentDetailedMapper {
    public GetAppointmentDetailedResponse toDto(Appointment appointment) {
        GetAppointmentPatientResponse patient = new GetAppointmentPatientResponse(
                appointment.getPatient().getId(),
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName(),
                appointment.getPatient().getEmail().getValue()
        );

        GetAppointmentDoctorResponse doctor = new GetAppointmentDoctorResponse(
                appointment.getDoctor().getId(),
                appointment.getDoctor().getPersonalInformations().getFirstName(),
                appointment.getDoctor().getPersonalInformations().getLastName(),
                appointment.getDoctor().getProfessionalInformations().getSpeciality(),
                appointment.getDoctor().getPersonalInformations().getProfilePictureUrl()
        );

        GetAppointmentAddressResponse address = new GetAppointmentAddressResponse(
                appointment.getDoctor().getConsultationInformations().getAddress(),
                appointment.getDoctor().getConsultationInformations().getCoordinatesGps().getClinicLatitude(),
                appointment.getDoctor().getConsultationInformations().getCoordinatesGps().getClinicLongitude()
        );

//        List<GetPreAppointmentAnswersResponse> answers = appointment
//                .getPreAppointmentAnswers()
//                .stream()
//                .map(preAppointmentAnswers -> new GetPreAppointmentAnswersResponse(
//                        preAppointmentAnswers.getQuestion().getQuestion(),
//                        preAppointmentAnswers.getResponse()
//                )).toList();

        return new GetAppointmentDetailedResponse(
                appointment.getId(),
                appointment.getMedicalConcern().getName(),
                appointment.getMedicalConcern().getPrice(),
                appointment.getDate(),
                appointment.getTakenAt(),
                appointment.getHoursRange().getStart(),
                appointment.getHoursRange().getEnd(),
                patient,
                doctor,
                address
//                answers
        );
    }
}
