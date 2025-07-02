package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account;

import jakarta.validation.constraints.NotBlank;

public record FetchAddressAutocompleteRequest(
        @NotBlank String address
) {
    public FetchAddressAutocompleteRequest {
        address = address.trim();
    }
}
