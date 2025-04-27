package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.model.user.UserRoles;
import fr.esgi.doctodocapi.use_cases.user.AuthenticateUser;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatePatient {
    private final AuthenticateUser authenticateUser;

    public AuthenticatePatient(AuthenticateUser authenticateUser) {
        this.authenticateUser = authenticateUser;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        return this.authenticateUser.loginUser(loginRequest, UserRoles.PATIENT.name());
    }

    public DoubleAuthenticationResponse validateDoubleAuthCode(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticateUser.validateDoubleAuth(validateDoubleAuthRequest);
    }
}
