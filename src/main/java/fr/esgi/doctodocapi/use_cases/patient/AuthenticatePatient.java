package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.GeneratorToken;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRoles;
import fr.esgi.doctodocapi.use_cases.user.AuthenticateUser;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service responsible for handling authentication logic specific to patients.
 * <p>
 * This includes login and two-factor authentication validation,
 * and retrieves associated {@link Patient} details when applicable.
 * </p>
 */
@Service
public class AuthenticatePatient {
    private static final int TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES = 120;

    private final AuthenticateUser authenticateUser;
    private final PatientRepository patientRepository;
    private final GeneratorToken generatorToken;

    /**
     * Constructs the service with its dependencies.
     *
     * @param authenticateUser  the generic user authentication use case
     * @param patientRepository the repository for accessing patient data
     * @param generatorToken    the service responsible for generating authentication tokens
     */
    public AuthenticatePatient(AuthenticateUser authenticateUser, PatientRepository patientRepository,
                               GeneratorToken generatorToken) {
        this.authenticateUser = authenticateUser;
        this.patientRepository = patientRepository;
        this.generatorToken = generatorToken;
    }

    /**
     * Performs the login process for a patient.
     * Delegates the logic to the generic user authentication service.
     *
     * @param loginRequest the login request containing credentials
     * @return a {@link LoginResponse} with information related to the authentication attempt
     */
    public LoginResponse login(LoginRequest loginRequest) {
        return this.authenticateUser.loginUser(loginRequest, UserRoles.PATIENT.name());
    }

    /**
     * Validates the 2FA (two-factor authentication) code for a patient,
     * generates a long-term token, and returns associated patient information.
     *
     * @param validateDoubleAuthRequest the request containing the 2FA code and email
     * @return a {@link DoubleAuthenticationUserResponse} containing token and patient profile data
     */
    public DoubleAuthenticationUserResponse validateDoubleAuthCode(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        User user = this.authenticateUser.validateDoubleAuth(validateDoubleAuthRequest);

        String token = this.generatorToken.generate(user.getEmail().getValue(), UserRoles.PATIENT.name(),
                TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES);

        Optional<Patient> optionalPatient = this.patientRepository.getByUserId(user.getId());
        UUID id = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        String phoneNumber = null;
        boolean hasOnBoardingDone = false;

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            id = patient.getId();
            email = patient.getEmail().getValue();
            firstName = patient.getFirstName();
            lastName = patient.getLastName();
            phoneNumber = patient.getPhoneNumber().getValue();
            hasOnBoardingDone = true;
        }

        return new DoubleAuthenticationUserResponse(
                id,
                token,
                hasOnBoardingDone,
                email,
                firstName,
                lastName,
                phoneNumber
        );
    }

}
