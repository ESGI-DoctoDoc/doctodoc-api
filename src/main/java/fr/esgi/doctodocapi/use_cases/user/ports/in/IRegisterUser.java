package fr.esgi.doctodocapi.use_cases.user.ports.in;

import fr.esgi.doctodocapi.use_cases.user.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.RegisterResponse;

public interface IRegisterUser {
    RegisterResponse register(RegisterRequest registerRequest);
}
