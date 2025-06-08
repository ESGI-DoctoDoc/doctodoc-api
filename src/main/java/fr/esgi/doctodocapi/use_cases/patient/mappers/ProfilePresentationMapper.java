package fr.esgi.doctodocapi.use_cases.patient.mappers;

import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

/**
 * Maps domain Patient model to a profile response DTO for presentation layer.
 */
@Service
public class ProfilePresentationMapper {
    /**
     * Converts a Patient domain model to a GetProfileResponse DTO.
     *
     * @param patient Patient instance to convert
     * @return A GetProfileResponse with patient data
     */
    public GetProfileResponse toDto(Patient patient) {
        return new GetProfileResponse(
                patient.getId(),
                patient.getGender().name(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthdate().getValue(),
                patient.getEmail().getValue(),
                patient.getPhoneNumber().getValue()
        );
    }
}
