package fr.esgi.doctodocapi.use_cases.doctor.ports.out;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchAddressAutocompleteResponse;

import java.util.List;

public interface AddressAutoCompleteInput {
    List<FetchAddressAutocompleteResponse> fetchSuggestions(String input);
}
