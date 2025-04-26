package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.dtos.requests.RegisterRequest;
import fr.esgi.doctodocapi.dtos.responses.RegisterResponse;
import fr.esgi.doctodocapi.exceptions.UserAlreadyExistException;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterUser {
    private final UserRepository userRepository;

    public RegisterUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        String email = registerRequest.email().toLowerCase().trim();
        String password = registerRequest.password().trim();
        String phoneNumber = registerRequest.phoneNumber().trim();

        boolean isExistUser = this.userRepository.isExistUser(email, phoneNumber);
        if (isExistUser) {
            throw new UserAlreadyExistException();
        }

        User user = User.create(email, password, phoneNumber);
        this.userRepository.save(user);
        // todo Pas censé envoyer un mail de validation genre même flow que les patients ?
        return new RegisterResponse(user.getId()); // todo pourquoi renvoyer l'id ?


    }
}
