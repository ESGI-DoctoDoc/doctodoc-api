package fr.esgi.doctodocapi.dtos.responses;

public record DoubleAuthenticationResponse(
        String token,
        boolean hasOnBoardingDone,
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
