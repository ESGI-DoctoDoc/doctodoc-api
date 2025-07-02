package fr.esgi.doctodocapi.use_cases.patient.make_appointment;


import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentAddressResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentDetailedResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses.GetAppointmentPatientResponse;
import fr.esgi.doctodocapi.use_cases.patient.utils.GetDoctorProfileUrl;
import org.springframework.stereotype.Service;

@Service
public class AppointmentDetailedMapper {

    private final GetDoctorProfileUrl getDoctorProfileUrl;

    public AppointmentDetailedMapper(GetDoctorProfileUrl getDoctorProfileUrl) {
        this.getDoctorProfileUrl = getDoctorProfileUrl;
    }

    public GetAppointmentDetailedResponse toDto(Appointment appointment) {
        GetAppointmentPatientResponse patient = new GetAppointmentPatientResponse(
                appointment.getPatient().getId(),
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName(),
                appointment.getPatient().getEmail().getValue()
        );

        String profileUrl = this.getDoctorProfileUrl.getUrl(appointment.getDoctor().getPersonalInformations().getProfilePictureUrl());

        GetAppointmentDoctorResponse doctor = new GetAppointmentDoctorResponse(
                appointment.getDoctor().getId(),
                appointment.getDoctor().getPersonalInformations().getFirstName(),
                appointment.getDoctor().getPersonalInformations().getLastName(),
                appointment.getDoctor().getProfessionalInformations().getSpeciality().getName(),
                profileUrl
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
