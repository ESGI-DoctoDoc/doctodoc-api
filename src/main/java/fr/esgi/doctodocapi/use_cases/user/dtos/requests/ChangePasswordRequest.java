package fr.esgi.doctodocapi.use_cases.user.dtos.requests;

public record ChangePasswordRequest(String newPassword, String oldPassword) {
}