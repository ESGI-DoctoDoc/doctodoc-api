package fr.esgi.doctodocapi.dtos.requests;

public record UpdatePasswordRequest(String newPassword, String token) {}