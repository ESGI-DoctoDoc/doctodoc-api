package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requests.patient.UpdateProfileRequest;
import fr.esgi.doctodocapi.dtos.responses.UpdateProfileResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.mappers.ProfilePresentationMapper;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ManageProfile {
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

    public UpdateProfileResponse update(UpdateProfileRequest updateProfileRequest) {
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
