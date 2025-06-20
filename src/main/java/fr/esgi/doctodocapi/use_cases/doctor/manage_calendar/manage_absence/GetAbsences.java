package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence;

import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response.GetAbsenceResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IGetAbsences;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Use case to retrieve all absences for the currently authenticated doctor.
 * <p>
 * This class fetches the current user's email, retrieves the corresponding doctor entity,
 * then returns a list of mapped absences from the domain to the response format.
 */
public class GetAbsences implements IGetAbsences {
    private final GetCurrentUserContext getCurrentUserContext;
    private final AbsenceRepository absenceRepository;
    private final UserRepository userRepository;
    private final AbsenceResponseMapper absenceResponseMapper;
    private final DoctorRepository doctorRepository;

    public GetAbsences(GetCurrentUserContext getCurrentUserContext, AbsenceRepository absenceRepository, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.absenceRepository = absenceRepository;
        this.userRepository = userRepository;
        this.absenceResponseMapper = absenceResponseMapper;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Executes the use case to retrieve all absences for the current doctor.
     *
     * @return a list of {@link GetAbsenceResponse} DTOs representing the doctor's absences
     * @throws ApiException if a domain-level error occurs
     */
    public List<GetAbsenceResponse> execute(int page, int size, LocalDate startDate) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<Absence> absences;
            if (startDate != null) {

                LocalDate endDate = startDate.plusDays(6);
                absences = this.absenceRepository.findAllByDoctorIdAndDateBetween(doctor.getId(), startDate, endDate, page, size);
            } else {

                absences = this.absenceRepository.findAllByDoctorIdAndStartDateAfterNow(doctor.getId(), LocalDate.now(), page, size);
            }

            return this.absenceResponseMapper.toResponseList(absences);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
