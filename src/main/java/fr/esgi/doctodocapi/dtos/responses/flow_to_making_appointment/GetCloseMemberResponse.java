package fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment;

import java.util.UUID;

public record GetCloseMemberResponse(
        UUID patientId,
        String fullName,
        Boolean isMain
) {
}
