package fr.esgi.doctodocapi.infrastructure.security.service;

import fr.esgi.doctodocapi.use_cases.exceptions.authentication.AuthenticationException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.AuthenticateUserInContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static fr.esgi.doctodocapi.use_cases.exceptions.authentication.AuthentificationMessageException.BAD_CREDENTIALS;

@Service
public class AuthenticateUserInContextImpl implements AuthenticateUserInContext {
    private final AuthenticationManager authenticationManager;

    public AuthenticateUserInContextImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void persistAuthentication(String username, String password) {
        try {
            Authentication authentication =
                    this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                            password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            throw new AuthenticationException(BAD_CREDENTIALS);
        }
    }
}
