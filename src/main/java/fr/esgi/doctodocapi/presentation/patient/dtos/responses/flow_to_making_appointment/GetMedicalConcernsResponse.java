package fr.esgi.doctodocapi.presentation.patient.dtos.responses.flow_to_making_appointment;

import java.util.UUID;

public record GetMedicalConcernsResponse(
        UUID id,
        String name
) {
}
