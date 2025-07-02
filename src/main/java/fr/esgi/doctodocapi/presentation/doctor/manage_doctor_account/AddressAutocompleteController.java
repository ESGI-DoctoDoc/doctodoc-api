package fr.esgi.doctodocapi.presentation.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.FetchAddressAutocompleteRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchAddressAutocompleteResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IAutocompleteAddress;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class AddressAutocompleteController {
    private final IAutocompleteAddress autocompleteAddress;

    public AddressAutocompleteController(IAutocompleteAddress autocompleteAddress) {
        this.autocompleteAddress = autocompleteAddress;
    }

    @GetMapping("autocomplete/address")
    public List<FetchAddressAutocompleteResponse> autocomplete(@Valid FetchAddressAutocompleteRequest request) {
        return autocompleteAddress.execute(request);
    }
}
