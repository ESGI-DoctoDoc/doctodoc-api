package fr.esgi.doctodocapi.use_cases.doctor.absence;

import fr.esgi.doctodocapi.dtos.requests.doctor.absence.SaveSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.absence.GetAbsenceResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidator;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for saving a single-day absence for the currently authenticated doctor.
 */
@Service
public class SaveSingleDayAbsence {
    private final AbsenceRepository absenceRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final AbsenceResponseMapper absenceResponseMapper;

    public SaveSingleDayAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper) {
        this.absenceRepository = absenceRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.absenceResponseMapper = absenceResponseMapper;
    }

    /**
     * Creates and persists a single-day absence, after validating it against existing absences.
     *
     * @param request the absence creation request
     * @return the saved absence as a response DTO
     * @throws ApiException if domain validation fails
     */
    public GetAbsenceResponse execute(SaveSingleDayAbsenceRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User doctor = this.userRepository.findByEmail(username);

            Absence absence = Absence.createSingleDate(request.description(), request.date(), doctor.getId());
            List<Absence> existing = this.absenceRepository.findAllByDoctorId(doctor.getId());

            AbsenceValidator.validateNoConflictWithExisting(absence, existing);

            Absence saved = this.absenceRepository.save(absence);
            return this.absenceResponseMapper.toResponse(saved);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
