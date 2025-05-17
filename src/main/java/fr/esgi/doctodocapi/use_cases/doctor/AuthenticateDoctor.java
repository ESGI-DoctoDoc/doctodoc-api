package fr.esgi.doctodocapi.use_cases.doctor;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.GeneratorToken;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRoles;
import fr.esgi.doctodocapi.use_cases.user.AuthenticateUser;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticateDoctor {
    private static final int TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES = 120;

    private final AuthenticateUser authenticateUser;
    private final GeneratorToken generatorToken;
    private final DoctorRepository doctorRepository;

    public AuthenticateDoctor(AuthenticateUser authenticateUser, GeneratorToken generatorToken, DoctorRepository doctorRepository) {
        this.authenticateUser = authenticateUser;
        this.generatorToken = generatorToken;
        this.doctorRepository = doctorRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        return this.authenticateUser.loginUser(loginRequest, UserRoles.DOCTOR.name());
    }

    public DoubleAuthenticationUserResponse validateDoubleAuthCode(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        User user = this.authenticateUser.validateDoubleAuth(validateDoubleAuthRequest);

        String token = this.generatorToken.generate(user.getEmail().getValue(), UserRoles.PATIENT.name(),
                TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES);

        Optional<Doctor> optionalDoctor = this.doctorRepository.getByUserId(user.getId());
        UUID id = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        String phoneNumber = null;
        boolean hasOnBoardingDone = false;

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            id = doctor.getId();
            email = doctor.getEmail().getValue();
            firstName = doctor.getPersonalInformations().getFirstName();
            lastName = doctor.getPersonalInformations().getLastName();
            phoneNumber = doctor.getPhoneNumber().getValue();
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
