package fr.esgi.doctodocapi.dtos.requets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginViaPhoneNumberRequest(
        @NotBlank
        @Size(min = 10, max = 10)
        String phoneNumber,

        @NotBlank
        String password
) {
}
