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

@Service
public class AuthenticatePatient {
    private static final int TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES = 120;

    private final AuthenticateUser authenticateUser;
    private final PatientRepository patientRepository;
    private final GeneratorToken generatorToken;


    public AuthenticatePatient(AuthenticateUser authenticateUser, PatientRepository patientRepository,
                               GeneratorToken generatorToken) {
        this.authenticateUser = authenticateUser;
        this.patientRepository = patientRepository;
        this.generatorToken = generatorToken;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        return this.authenticateUser.loginUser(loginRequest, UserRoles.PATIENT.name());
    }

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
