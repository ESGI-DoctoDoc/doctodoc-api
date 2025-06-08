package fr.esgi.doctodocapi.use_cases.patient.mappers;

import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

@Service
public class ProfilePresentationMapper {
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
