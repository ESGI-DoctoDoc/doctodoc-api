package fr.esgi.doctodocapi.dtos.responses.making_appointment;

import java.util.UUID;

public record GetMedicalConcernsResponse(
        UUID id,
        String name
) {
}
