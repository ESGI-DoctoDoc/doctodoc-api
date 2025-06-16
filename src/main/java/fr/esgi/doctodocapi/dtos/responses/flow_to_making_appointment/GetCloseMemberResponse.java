package fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment;

import java.util.UUID;

public record GetCloseMemberResponse(
        UUID id,
        String lastName,
        String firstName,
        String email,
        String gender,
        String phoneNumber,
        String birthdate,
        Boolean isMain
) {
}
