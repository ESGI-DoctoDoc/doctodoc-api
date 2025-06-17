package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetDoctorAppointmentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.PatientInfo;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AppointmentResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public GetDoctorAppointmentResponse toResponse(Appointment appointment) {
        String fullName = String.format("%s %s",
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName());

        return new GetDoctorAppointmentResponse(
                appointment.getId(),
                new PatientInfo(
                        appointment.getPatient().getId(),
                        fullName,
                        appointment.getPatient().getEmail().getValue(),
                        appointment.getPatient().getPhoneNumber().getValue(),
                        appointment.getPatient().getBirthdate().getValue().toString()
                ),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().toString(),
                appointment.getStatus().getValue(),
                appointment.getDoctorNotes(),
                formatDate(appointment.getTakenAt())
        );
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }
}