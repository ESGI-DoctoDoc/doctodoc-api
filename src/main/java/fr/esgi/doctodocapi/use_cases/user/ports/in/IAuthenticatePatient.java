package fr.esgi.doctodocapi.use_cases.user.ports.in;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.LoginResponse;

public interface IAuthenticatePatient {
    LoginResponse login(LoginRequest loginRequest);
    DoubleAuthenticationUserResponse validateDoubleAuthCode(ValidateDoubleAuthRequest validateDoubleAuthRequest);
}
