package fr.esgi.doctodocapi.infrastructure.services.external.dtos.auto_complete;

import java.util.List;

public record AutoCompleteAddressApiResponse(List<Feature> features) {
}
