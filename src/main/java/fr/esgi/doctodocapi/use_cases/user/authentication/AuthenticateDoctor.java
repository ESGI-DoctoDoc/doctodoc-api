package fr.esgi.doctodocapi.use_cases.user.authentication;

import fr.esgi.doctodocapi.model.admin.AdminRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.user.TokenManager;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRoles;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.DoubleAuthenticationUserResponse;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.LoginRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.requests.ValidateDoubleAuthRequest;
import fr.esgi.doctodocapi.use_cases.user.dtos.responses.LoginResponse;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IAuthenticateDoctor;
import fr.esgi.doctodocapi.use_cases.user.ports.in.IAuthenticateUser;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;

import java.util.Optional;
import java.util.UUID;

/**
 * Service responsible for doctor authentication operations.
 * This class handles doctor login and two-factor authentication validation,
 * providing secure access to doctor-specific functionality in the application.
 */
public class AuthenticateDoctor implements IAuthenticateDoctor {
    /**
     * The expiration time in minutes for long-term authentication tokens.
     */
    private static final int TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES = 120;

    /**
     * Service for authenticating users, used to delegate general user authentication operations.
     */
    private final IAuthenticateUser authenticateUser;

    /**
     * Service for generating authentication tokens.
     */
    private final TokenManager tokenManager;

    /**
     * Repository for accessing doctor data.
     */
    private final DoctorRepository doctorRepository;

    private final AdminRepository adminRepository;

    /**
     * Constructs an AuthenticateDoctor service with the required dependencies.
     *
     * @param authenticateUser Service for general user authentication operations
     * @param tokenManager     Service for generating authentication tokens
     * @param doctorRepository Repository for accessing doctor data
     */
    public AuthenticateDoctor(IAuthenticateUser authenticateUser, TokenManager tokenManager, DoctorRepository doctorRepository, AdminRepository adminRepository) {
        this.authenticateUser = authenticateUser;
        this.tokenManager = tokenManager;
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
    }

    /**
     * Authenticates a doctor using their login credentials.
     * This method delegates to the general user authentication service,
     * specifying the DOCTOR role for the authentication process.
     *
     * @param loginRequest The request containing the doctor's login credentials
     * @return A response containing authentication information, including a token for double authentication
     */
    public LoginResponse login(LoginRequest loginRequest) {
        return this.authenticateUser.loginUser(loginRequest);
    }

    /**
     * Validates a doctor's double authentication code and generates a long-term authentication token.
     * This method first validates the double authentication code through the general user authentication service,
     * then retrieves the doctor's information if available, and generates a response with the doctor's details
     * and a long-term authentication token.
     *
     * @param validateDoubleAuthRequest The request containing the double authentication code to validate
     * @return A response containing the doctor's information and a long-term authentication token
     */
    public DoubleAuthenticationUserResponse validateDoubleAuthCode(ValidateDoubleAuthRequest validateDoubleAuthRequest) {
        User user = this.authenticateUser.validateDoubleAuth(validateDoubleAuthRequest);

        String role;
        if (adminRepository.existsByUserId(user.getId())) {
            role = UserRoles.ADMIN.name();
        } else {
            role = UserRoles.DOCTOR.name();
        }

        String token = this.tokenManager.generate(user.getEmail().getValue(), role, TOKEN_LONG_TERM_EXPIRATION_IN_MINUTES);

        // DOCTOR
        if (role.equals(UserRoles.DOCTOR.name())) {
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

        // ADMIN
        return new DoubleAuthenticationUserResponse(
                user.getId(),
                token,
                true,
                user.getEmail().getValue(),
                null,
                null,
                user.getPhoneNumber().getValue()
        );
    }
}
