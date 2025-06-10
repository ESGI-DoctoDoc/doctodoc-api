package fr.esgi.doctodocapi.domain.use_cases.patient.manage_close_member;

import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.domain.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.domain.DomainException;
import fr.esgi.doctodocapi.domain.entities.patient.Patient;
import fr.esgi.doctodocapi.domain.entities.patient.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCloseMember {
    private final PatientRepository patientRepository;

    public GetCloseMember(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public GetCloseMemberResponse get(UUID id) {
        try {

            Patient closeMember = this.patientRepository.getById(id);
            return new GetCloseMemberResponse(closeMember.getId(), closeMember.getLastName(), closeMember.getFirstName(), closeMember.getEmail().getValue(), closeMember.getGender().name(), closeMember.getPhoneNumber().getValue(), closeMember.getBirthdate().getValue().toString(), false);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
