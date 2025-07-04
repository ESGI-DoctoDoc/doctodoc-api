package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.DeleteAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IDeleteAbsence;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Use case for deleting a doctor's absence by its unique identifier.
 */
public class DeleteAbsence implements IDeleteAbsence {
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