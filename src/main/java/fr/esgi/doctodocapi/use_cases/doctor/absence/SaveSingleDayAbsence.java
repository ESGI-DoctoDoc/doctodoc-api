package fr.esgi.doctodocapi.use_cases.doctor.absence;

import fr.esgi.doctodocapi.dtos.requests.doctor.absence.SaveSingleDayAbsenceRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.absence.GetAbsenceResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidationService;
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
    private final DoctorRepository doctorRepository;
    private final AbsenceValidationService absenceValidationService;

    public SaveSingleDayAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository, AbsenceValidationService absenceValidationService) {
        this.absenceRepository = absenceRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.absenceResponseMapper = absenceResponseMapper;
        this.doctorRepository = doctorRepository;
        this.absenceValidationService = absenceValidationService;
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
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Absence absence = Absence.createSingleDate(request.description(), request.date());
            List<Absence> existing = this.absenceRepository.findAllByDoctorId(doctor.getId());

            this.absenceValidationService.validateNoConflictWithExisting(absence, existing);

            doctor.getCalendar().addAbsence(absence);

            Absence saved = this.absenceRepository.save(absence, doctor.getId());
            return this.absenceResponseMapper.toResponse(saved);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
