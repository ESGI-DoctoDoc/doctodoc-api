package fr.esgi.doctodocapi.use_cases.doctor.absence;

import fr.esgi.doctodocapi.dtos.responses.doctor.absence.DeleteAbsenceResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting a doctor's absence by its unique identifier.
 */
@Service
public class DeleteAbsence {
    private final AbsenceRepository absenceRepository;

    public DeleteAbsence(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    /**
     * Deletes the absence identified by the given ID.
     *
     * @param absenceId the UUID of the absence to delete
     * @return a confirmation response after deletion
     * @throws ApiException if domain validation fails or the absence doesn't exist
     */
    public DeleteAbsenceResponse execute(UUID absenceId) {
        try {
            this.absenceRepository.delete(absenceId);
            return new DeleteAbsenceResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}