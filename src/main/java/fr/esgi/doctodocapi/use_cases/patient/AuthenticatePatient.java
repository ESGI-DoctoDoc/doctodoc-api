package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.requets.LoginViaEmailRequest;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatePatient {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;


    public AuthenticatePatient(AuthenticationManager authenticationManager, UserRepository userRepository, PatientRepository patientRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }


    public String loginWithEmail(LoginViaEmailRequest loginViaEmailRequest) {
        String email = loginViaEmailRequest.email().trim();
        String password = loginViaEmailRequest.password().trim();
        this.authenticate(email, password);

        User userFoundByEmail = this.userRepository.findByEmail(email);

        if (!userFoundByEmail.isEmailVerified()) {
            this.sendEmailToValidateAccount();
            return "send email to activate your account";
        } else {
            boolean isPatientExist = this.patientRepository.isExistPatientByUserId(userFoundByEmail.getId());
            if (isPatientExist) {
                this.sendMessageWithDoubleAuthCode();
                return "send message to validate double auth code";
            } else {
                throw new AuthenticationException();
            }
        }
    }


    private void authenticate(String username, String password) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    private void sendEmailToValidateAccount() {
        System.out.println("Sending email to validate account...");

    }

    private void sendMessageWithDoubleAuthCode() {
        System.out.println("Sending message with double auth code...");

    }

}
