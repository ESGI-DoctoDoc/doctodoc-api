package fr.esgi.doctodocapi.use_cases.user.dtos.requests;

public record UpdatePasswordRequest(String newPassword, String token) {}