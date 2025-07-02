package fr.esgi.doctodocapi.infrastructure.api_address.dtos.auto_complete_response;

import java.util.List;

public record AutoCompleteAddressApiResponse(List<Feature> features) {
}
