package fr.esgi.doctodocapi.dtos.requests.doctor;

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
        @NotBlank String profilePictureUrl,
        @NotEmpty List<String> languages,
        @NotEmpty List<String> doctorDocuments,
        @NotBlank String gender
        ){
    public OnBoardingDoctorRequest {
        rpps = rpps.trim();
        speciality = speciality.trim();
        firstName = firstName.trim();
        lastName = lastName.trim();
        bio = bio.trim();
        profilePictureUrl = profilePictureUrl.trim();
        languages = languages.stream().map(String::trim).toList();
        doctorDocuments = doctorDocuments.stream().map(String::trim).toList();
    }
}
