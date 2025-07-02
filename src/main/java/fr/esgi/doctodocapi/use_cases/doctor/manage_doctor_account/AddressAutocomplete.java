package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.FetchAddressAutocompleteRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchAddressAutocompleteResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IAutocompleteAddress;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.AddressAutoCompleteInput;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class AddressAutocomplete implements IAutocompleteAddress {

    private final AddressAutoCompleteInput addressAutoCompleteInput;

    public AddressAutocomplete(AddressAutoCompleteInput addressAutoCompleteInput) {
        this.addressAutoCompleteInput = addressAutoCompleteInput;
    }

    public List<FetchAddressAutocompleteResponse> execute(FetchAddressAutocompleteRequest request) {
        try {
            return this.addressAutoCompleteInput.fetchSuggestions(request.address());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
