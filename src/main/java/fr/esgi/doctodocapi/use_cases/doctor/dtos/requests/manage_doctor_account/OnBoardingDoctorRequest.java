package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record OnBoardingDoctorRequest (
        @NotBlank String rpps,
        @NotBlank String speciality,
        @NotNull Short experienceYears,
        @NotNull Boolean acceptPublicCoverage,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate birthDate,
        @NotBlank String bio,
        @NotBlank String pictureDocumentId,
        @NotEmpty List<String> languages,
        @NotEmpty List<String> doctorDocuments,
        @NotBlank String gender,
        @NotBlank String address
        ){
    public OnBoardingDoctorRequest {
        rpps = rpps.trim();
        speciality = speciality.trim();
        firstName = firstName.trim();
        lastName = lastName.trim();
        bio = bio.trim();
        languages = languages.stream().map(String::trim).toList();
        address = address.trim();
    }
}
