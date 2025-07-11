package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.admin.AdminRepository;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.personal_information.CoordinatesGps;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.notification.NotificationsType;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.OnBoardingDoctorRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.FetchCoordinatesResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.OnboardingProcessResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IOnboardingDoctor;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.AddressCoordinatesFetcher;
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
    private final AdminRepository adminRepository;
    private final NotificationRepository notificationRepository;
    private final SpecialityRepository specialityRepository;
    private final AddressCoordinatesFetcher addressCoordinatesFetcher;

    /**
     * Constructs the service with the required dependencies.
     *
     * @param doctorRepository      the repository to manage doctor entities
     * @param userRepository        the repository to manage user entities
     * @param getCurrentUserContext component to retrieve the currently authenticated user context
     */
    public OnboardingDoctorProcess(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DocumentRepository documentRepository, AdminRepository adminRepository, NotificationRepository notificationRepository, SpecialityRepository specialityRepository, AddressCoordinatesFetcher addressCoordinatesFetcher) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.documentRepository = documentRepository;
        this.adminRepository = adminRepository;
        this.notificationRepository = notificationRepository;
        this.specialityRepository = specialityRepository;
        this.addressCoordinatesFetcher = addressCoordinatesFetcher;
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

            UUID profilePictureId = UUID.fromString(request.pictureDocumentId());
            Document profilePicture = this.documentRepository.getById(profilePictureId);

            if (!profilePicture.getType().equals(DocumentType.PROFILE_PICTURE)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "document.invalid_type", "Le document fourni n'est pas une photo de profil valide.");
            }

            FetchCoordinatesResponse coordinatesResponse = this.addressCoordinatesFetcher.fetchCoordinates(request.address());
            CoordinatesGps coordinates = CoordinatesGps.of(
                    coordinatesResponse.latitude(),
                    coordinatesResponse.longitude()
            );

            String profilePictureUrl = profilePicture.getPath();

            Speciality speciality = this.specialityRepository.findByName(request.speciality());

            Doctor doctor = Doctor.createFromOnBoarding(user, request, uploadedDocuments, profilePictureUrl, speciality, coordinates);

            this.doctorRepository.save(doctor);
            notifyAdmin();
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

    private void notifyAdmin() {
        List<UUID> adminsId = this.adminRepository.getAll();
        adminsId.forEach(id -> {
            Notification notification = NotificationsType.verifyDoctor(id);
            this.notificationRepository.save(notification);
        });
    }
}