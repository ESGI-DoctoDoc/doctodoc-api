package fr.esgi.doctodocapi.use_cases.patient.manage_close_member;

import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCloseMember {
    private final PatientRepository patientRepository;

    public DeleteCloseMember(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public void process(UUID closeMemberId) {
        try {
            this.patientRepository.delete(closeMemberId);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }
}
