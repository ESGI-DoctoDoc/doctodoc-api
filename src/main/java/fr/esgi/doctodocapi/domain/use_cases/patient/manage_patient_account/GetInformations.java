package fr.esgi.doctodocapi.domain.use_cases.patient.manage_patient_account;

import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.domain.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.domain.DomainException;
import fr.esgi.doctodocapi.domain.entities.patient.Patient;
import fr.esgi.doctodocapi.domain.entities.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.domain.entities.patient.PatientRepository;
import fr.esgi.doctodocapi.domain.entities.user.User;
import fr.esgi.doctodocapi.domain.entities.user.UserRepository;
import fr.esgi.doctodocapi.domain.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving patient-related information for the authenticated user.
 * <p>
 * Provides operations such as fetching the patient's basic profile data,
 * and listing any registered close members.
 * </p>
 */
@Service
public class GetInformations {
    private final PatientRepository patientRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final ProfilePresentationMapper profilePresentationMapper;

    /**
     * Constructs the service with required dependencies.
     *
     * @param patientRepository     repository for accessing patient entities
     * @param getCurrentUserContext interface to access the currently authenticated user's context
     * @param userRepository        repository for accessing user entities
     */
    public GetInformations(PatientRepository patientRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, ProfilePresentationMapper profilePresentationMapper) {
        this.patientRepository = patientRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.profilePresentationMapper = profilePresentationMapper;
    }

    /**
     * Retrieves basic personal information for the currently authenticated patient.
     *
     * @return a {@link GetProfileResponse} DTO containing basic patient details
     * @throws ApiException if the patient is not found or another domain exception occurs
     */
    public GetProfileResponse getBasicPatientInfo() {
        try {
            String email = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(email);

            Patient patient = this.patientRepository.getByUserId(user.getId())
                    .orElseThrow(PatientNotFoundException::new);

            return this.profilePresentationMapper.toDto(patient);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
