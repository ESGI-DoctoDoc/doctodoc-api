package fr.esgi.doctodocapi.use_cases.doctor.manage_absence;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case to retrieve all absences for the currently authenticated doctor.
 * <p>
 * This class fetches the current user's email, retrieves the corresponding doctor entity,
 * then returns a list of mapped absences from the domain to the response format.
 */
@Service
public class GetAbsences {
    private final GetCurrentUserContext getCurrentUserContext;
    private final AbsenceRepository absenceRepository;
    private final UserRepository userRepository;
    private final AbsenceResponseMapper absenceResponseMapper;

    public GetAbsences(GetCurrentUserContext getCurrentUserContext, AbsenceRepository absenceRepository, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.absenceRepository = absenceRepository;
        this.userRepository = userRepository;
        this.absenceResponseMapper = absenceResponseMapper;
    }

    /**
     * Executes the use case to retrieve all absences for the current doctor.
     *
     * @return a list of {@link GetAbsenceResponse} DTOs representing the doctor's absences
     * @throws ApiException if a domain-level error occurs
     */
    public List<GetAbsenceResponse> execute() {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User doctor = this.userRepository.findByEmail(username);

            List<Absence> absences = this.absenceRepository.findAllByDoctorId(doctor.getId());
            return this.absenceResponseMapper.toResponseList(absences);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
