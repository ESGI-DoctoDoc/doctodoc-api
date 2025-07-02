package fr.esgi.doctodocapi.infrastructure.services.external;

import fr.esgi.doctodocapi.infrastructure.services.external.dtos.auto_complete.AutoCompleteAddressApiResponse;
import fr.esgi.doctodocapi.infrastructure.services.external.dtos.fetch_response.FetchCoordinatesApiResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchAddressAutocompleteResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchCoordinatesResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.AddressAutoCompleteInput;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.AddressCoordinatesFetcher;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class AddressAutocompleteService implements AddressAutoCompleteInput, AddressCoordinatesFetcher {

    private final RestTemplate restTemplate;

    public AddressAutocompleteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<FetchAddressAutocompleteResponse> fetchSuggestions(String input) {
        if (input == null || input.trim().length() < 3) {
            return Collections.emptyList();
        }

        String addressApiUrl = "https://api-adresse.data.gouv.fr/search";
        String url = String.format("%s?q=%s&limit=5", addressApiUrl, input.trim().replace(" ", "+"));

        try {
            AutoCompleteAddressApiResponse response = this.restTemplate.getForObject(url, AutoCompleteAddressApiResponse.class);

            if (response != null && response.features() != null) {
                return response.features().stream()
                        .map(feature -> new FetchAddressAutocompleteResponse(feature.properties().label()))
                        .toList();
            }

            return Collections.emptyList();
        } catch (RestClientException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "address.api.error", e.getMessage());
        }
    }

    public FetchCoordinatesResponse fetchCoordinates(String address) {
        String url = "https://api-adresse.data.gouv.fr/search/?q=" + address.trim().replace(" ", "+") + "&limit=1";

        try {
            FetchCoordinatesApiResponse response = this.restTemplate.getForObject(url, FetchCoordinatesApiResponse.class);

            if (response != null && !response.features().isEmpty()) {
                var coordinates = response.features().getFirst().geometry().coordinates();

                if (coordinates.size() >= 2) {
                    return new FetchCoordinatesResponse(
                            BigDecimal.valueOf(coordinates.get(1)),
                            BigDecimal.valueOf(coordinates.get(0))
                    );
                }
            }
            throw new ApiException(HttpStatus.BAD_REQUEST, "address.coordinates.not_found", "Coordonnées GPS introuvables pour cette adresse");
        } catch (RestClientException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "address.api.error", e.getMessage());
        }
    }
}