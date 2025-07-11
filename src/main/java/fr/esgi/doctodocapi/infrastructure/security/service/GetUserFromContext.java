package fr.esgi.doctodocapi.infrastructure.security.service;

import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.IGetUserFromContext;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.stereotype.Service;

@Service
public class GetUserFromContext implements IGetUserFromContext {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;

    public GetUserFromContext(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
    }

    public User get() {
        String username = this.getCurrentUserContext.getUsername();
        return this.userRepository.findByEmail(username);
    }
}
