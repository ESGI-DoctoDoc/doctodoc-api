package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.*;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentResponseMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public GetDoctorAppointmentResponse toResponse(Appointment appointment) {
        String fullName = getPatientFullName(appointment);

        return new GetDoctorAppointmentResponse(
                appointment.getId(),
                new PatientInfo(
                        appointment.getPatient().getId(),
                        fullName,
                        appointment.getPatient().getEmail().getValue(),
                        appointment.getPatient().getPhoneNumber().getValue(),
                        appointment.getPatient().getBirthdate().getValue().toString()
                ),
                new MedicalConcernInfo(
                        appointment.getMedicalConcern().getId(),
                        appointment.getMedicalConcern().getName()
                ),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().toString(),
                appointment.getHoursRange().getEnd().toString(),
                appointment.getStatus().getValue(),
                appointment.getDoctorNotes(),
                formatDate(appointment.getTakenAt())
        );
    }

    public GetDoctorAppointmentDetailsResponse toDetailsResponse(Appointment appointment, CareTracking careTracking) {
        String fullName = getPatientFullName(appointment);

        GetCareTrackingResponse careTrackingResponse = careTracking != null
                ? GetCareTrackingResponse.fromDomain(careTracking)
                : null;

        return new GetDoctorAppointmentDetailsResponse(
                appointment.getId(),
                new PatientInfo(
                        appointment.getPatient().getId(),
                        fullName,
                        appointment.getPatient().getEmail().getValue(),
                        appointment.getPatient().getPhoneNumber().getValue(),
                        appointment.getPatient().getBirthdate().getValue().toString()
                ),
                new MedicalConcernInfo(
                        appointment.getMedicalConcern().getId(),
                        appointment.getMedicalConcern().getName()
                ),
                careTrackingResponse,
                mapAnswers(appointment),
                appointment.getDate().toString(),
                appointment.getHoursRange().getStart().toString(),
                appointment.getStatus().getValue(),
                appointment.getDoctorNotes(),
                formatDate(appointment.getTakenAt())
        );
    }

    private String getPatientFullName(Appointment appointment) {
        return String.format("%s %s",
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName());
    }

    private List<AnswerInfo> mapAnswers(Appointment appointment) {
        return appointment.getPreAppointmentAnswers() != null
                ? appointment.getPreAppointmentAnswers().stream()
                .map(answer ->
                        new AnswerInfo(
                                answer.getQuestion().getId(),
                                answer.getResponse()
                        )
                ).toList() : null;
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }
}