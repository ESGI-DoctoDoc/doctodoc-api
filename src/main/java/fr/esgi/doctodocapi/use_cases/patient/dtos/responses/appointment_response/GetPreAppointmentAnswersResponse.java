package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_response;

public record GetPreAppointmentAnswersResponse(
        String question,
        String answer
) {
}
