package fr.esgi.doctodocapi.use_cases.doctor;

import fr.esgi.doctodocapi.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.dtos.responses.DoubleAuthenticationResponse;
import fr.esgi.doctodocapi.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.model.user.UserRoles;
import fr.esgi.doctodocapi.use_cases.user.AuthenticateUser;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateDoctor {
    private final AuthenticateUser authenticateUser;

    public AuthenticateDoctor(AuthenticateUser authenticateUser) {
        this.authenticateUser = authenticateUser;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        return this.authenticateUser.loginUser(loginRequest, UserRoles.DOCTOR.name());
    }

    public DoubleAuthenticationResponse validateDoubleAuthCode(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        return this.authenticateUser.validateDoubleAuth(validateDoubleAuthRequest);
    }
}
