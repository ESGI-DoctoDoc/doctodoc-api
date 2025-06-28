package fr.esgi.doctodocapi.use_cases.user.ports.in;

import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.LoginResponse;

public interface IAuthenticateUser {
    LoginResponse loginUser(LoginRequest loginRequest, String role);
    User validateDoubleAuth(ValidateDoubleAuthRequest validateDoubleAuthRequest);
}
