package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAppointmentOnAbsenceResponse;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentForAbsenceMapper {

    private static final DateTimeFormatter HOUR_MIN_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public List<GetAppointmentOnAbsenceResponse> toResponseList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toResponse)
                .toList();
    }

    public GetAppointmentOnAbsenceResponse toResponse(Appointment appointment) {
        return new GetAppointmentOnAbsenceResponse(
                appointment.getId(),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().format(HOUR_MIN_FORMATTER),
                appointment.getHoursRange().getEnd().format(HOUR_MIN_FORMATTER),
                new GetAppointmentOnAbsenceResponse.AppointmentPatient(
                        appointment.getPatient().getId(),
                        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName()
                )
        );
    }
}