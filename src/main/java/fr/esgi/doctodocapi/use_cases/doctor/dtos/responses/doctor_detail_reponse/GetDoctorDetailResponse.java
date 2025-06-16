package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse;

import java.util.List;
import java.util.UUID;

public record GetDoctorDetailResponse(
        UUID id,
        String firstName,
        String lastName,
        String speciality,
        String biography,
        String pictureUrl,
        String rpps,
        GetAddressDoctorResponse address,
        List<String> languages,
        List<OpeningHoursResponse> openingHours
) {
}
