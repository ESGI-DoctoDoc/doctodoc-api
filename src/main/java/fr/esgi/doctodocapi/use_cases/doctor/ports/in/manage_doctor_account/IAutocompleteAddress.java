package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.FetchAddressAutocompleteRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchAddressAutocompleteResponse;

import java.util.List;

public interface IAutocompleteAddress {
    List<FetchAddressAutocompleteResponse> execute(FetchAddressAutocompleteRequest request);
}
