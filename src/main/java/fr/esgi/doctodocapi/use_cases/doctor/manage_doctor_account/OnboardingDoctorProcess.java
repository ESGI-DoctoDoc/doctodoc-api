package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.OnboardingProcessResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IOnboardingDoctor;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.on_boarding.DoctorAccountAlreadyExist;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

/**
 * Service handling the onboarding process of a doctor.
 * <p>
 * This service verifies if the authenticated user already has a doctor account,
 * and if not, it creates and saves a new {@link Doctor} entity based on the onboarding request.
 * </p>
 */
public class OnboardingDoctorProcess implements IOnboardingDoctor {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DocumentRepository documentRepository;

    /**
     * Constructs the service with the required dependencies.
     *
     * @param doctorRepository      the repository to manage doctor entities
     * @param userRepository        the repository to manage user entities
     * @param getCurrentUserContext component to retrieve the currently authenticated user context
     */
    public OnboardingDoctorProcess(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DocumentRepository documentRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.documentRepository = documentRepository;
    }

    /**
     * Executes the onboarding process for the currently authenticated user.
     * <p>
     * If a doctor account already exists for the user, an exception is thrown.
     * Otherwise, a new doctor account is created and saved.
     * </p>
     *
     * @param request the onboarding request containing necessary doctor details
     * @return an {@link OnboardingProcessResponse} with the user's ID
     * @throws ApiException              in case of a domain validation error
     * @throws DoctorAccountAlreadyExist if a doctor account already exists for the user
     */
    public OnboardingProcessResponse process(OnBoardingDoctorRequest request) {
        User user = this.getAuthenticatedUserOrThrow();

        try {
            boolean doctorAlreadyExist = this.doctorRepository.isExistsById(user.getId());
            if (doctorAlreadyExist) {
                throw new DoctorAccountAlreadyExist();
            }

            List<Document> uploadedDocuments = request.doctorDocuments().stream()
                    .map(UUID::fromString)
                    .map(this.documentRepository::getById)
                    .toList();

            Doctor doctor = Doctor.createFromOnBoarding(user, request, uploadedDocuments);
            this.doctorRepository.save(doctor);
            return new OnboardingProcessResponse(user.getId());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    /**
     * Retrieves the currently authenticated user or throws an exception if not found.
     *
     * @return the authenticated {@link User}
     * @throws UserNotFoundException if the user is not found by email
     */
    private User getAuthenticatedUserOrThrow() {
        String email = this.getCurrentUserContext.getUsername();

        try {
            return this.userRepository.findByEmail(email);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}