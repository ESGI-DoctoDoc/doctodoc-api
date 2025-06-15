    package fr.esgi.doctodocapi.infrastructure.mappers;

    import fr.esgi.doctodocapi.dtos.responses.doctor.appointment.GetDoctorAppointmentResponse;
    import fr.esgi.doctodocapi.dtos.responses.doctor.appointment.PatientInfo;
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