package fr.esgi.doctodocapi.use_cases.patient.mappers;

import fr.esgi.doctodocapi.dtos.responses.UpdateProfileResponse;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

@Service
public class ProfilePresentationMapper {
    public UpdateProfileResponse toDto(Patient patient) {
        return new UpdateProfileResponse(
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
