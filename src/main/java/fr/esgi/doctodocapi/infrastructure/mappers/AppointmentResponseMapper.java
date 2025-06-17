    package fr.esgi.doctodocapi.infrastructure.mappers;

    import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;
    import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.PatientInfo;
    import fr.esgi.doctodocapi.model.appointment.Appointment;
    import org.springframework.stereotype.Service;

    @Service
    public class AppointmentResponseMapper {

        public GetDoctorAppointmentResponse toResponse(Appointment appointment) {
            return new GetDoctorAppointmentResponse(
                    appointment.getId(),
                    new PatientInfo(
                            appointment.getPatient().getId(),
                            appointment.getPatient().getFirstName(),
                            appointment.getPatient().getLastName(),
                            appointment.getPatient().getEmail().getValue(),
                            appointment.getPatient().getPhoneNumber().getValue(),
                            appointment.getPatient().getBirthdate().getValue().toString()
                    ),
                    appointment.getDate().toString(),
                    appointment.getHoursRange().getStart().toString(),
                    appointment.getStatus().name(),
                    appointment.getDoctorNotes(),
                    appointment.getCreatedAt()
            );
        }
    }