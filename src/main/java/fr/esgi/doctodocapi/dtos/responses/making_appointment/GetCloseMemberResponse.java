package fr.esgi.doctodocapi.dtos.responses.making_appointment;

import java.util.UUID;

public record GetCloseMemberResponse(
        UUID patientId,
        String fullName,
        Boolean isMain
) {
}
