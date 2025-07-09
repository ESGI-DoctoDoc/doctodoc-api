package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientDetailResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.PatientAppointmentResponse;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PatientDetailResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public GetDoctorPatientDetailResponse toResponse(Patient patient, List<Appointment> appointments) {
        return new GetDoctorPatientDetailResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail().getValue(),
                patient.getPhoneNumber().getValue(),
                patient.getBirthdate().getValue().toString(),
                patient.getGender().name(),
                appointments.stream()
                        .map(appointment -> new PatientAppointmentResponse(
                                appointment.getId(),
                                appointment.getDate().format(DATE_FORMATTER),
                                appointment.getHoursRange().getStart().toString(),
                                appointment.getHoursRange().getEnd().toString(),
                                appointment.getCancelExplanation(),
                                appointment.getDoctorNotes(),
                                appointment.getStatus().getValue()
                        ))
                        .toList(),
                patient.getCreatedAt().format(DATE_FORMATTER)
        );
    }
}