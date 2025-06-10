package fr.esgi.doctodocapi.use_cases.patient.manage_account;

import fr.esgi.doctodocapi.dtos.requests.patient.UpdateProfileRequest;
import fr.esgi.doctodocapi.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IManageProfile;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service responsible for managing a patient's profile.
 * Provides functionality to update the profile information of the currently authenticated patient.
 */
public class ManageProfile implements IManageProfile {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final ProfilePresentationMapper profilePresentationMapper;

    public ManageProfile(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, ProfilePresentationMapper profilePresentationMapper) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.profilePresentationMapper = profilePresentationMapper;
    }

    /**
     * Updates the profile of the currently authenticated patient.
     * If the patient does not exist or if the update data is invalid, appropriate exceptions are thrown.
     *
     * @param updateProfileRequest DTO containing the new profile data
     * @return A response DTO with the updated profile information
     * @throws ApiException if any domain-related validation fails
     */
    public GetProfileResponse update(UpdateProfileRequest updateProfileRequest) {
        String username = this.getCurrentUserContext.getUsername();

        String firstName = updateProfileRequest.firstName();
        String lastName = updateProfileRequest.lastName();
        String gender = updateProfileRequest.gender();
        LocalDate birthdate = updateProfileRequest.birthdate();

        try {

            User user = this.userRepository.findByEmail(username);
            Optional<Patient> patient = this.patientRepository.getByUserId(user.getId());
            if (patient.isEmpty()) throw new PatientNotFoundException();

            patient.get().updateMainAccount(firstName, lastName, gender, birthdate);

            this.patientRepository.update(patient.get());
            return this.profilePresentationMapper.toDto(patient.get());

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
