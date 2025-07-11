package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account;

public record GetUpdatedProfileResponse(
        String firstName,
        String lastName,
        String bio,
        String address
){
}
