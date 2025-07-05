package fr.esgi.doctodocapi.use_cases.patient.manage_account;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveTokenFcmRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetProfileResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IGetInformations;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.NotificationService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.TokenFcmRepository;
import org.springframework.http.HttpStatus;

/**
 * Service for retrieving patient-related information for the authenticated user.
 * <p>
 * Provides operations such as fetching the patient's basic profile data,
 * and listing any registered close members.
 * </p>
 */
public class GetInformations implements IGetInformations {
    private final GetPatientFromContext getPatientFromContext;
    private final TokenFcmRepository tokenFcmRepository;
    private final ProfilePresentationMapper profilePresentationMapper;
    private final NotificationService notificationService;

    /**
     * Constructs the service with required dependencies.
     * @param getPatientFromContext interface to access the currently authenticated user's context
     */
    public GetInformations(GetPatientFromContext getPatientFromContext, TokenFcmRepository tokenFcmRepository, ProfilePresentationMapper profilePresentationMapper, NotificationService notificationService) {
        this.getPatientFromContext = getPatientFromContext;
        this.tokenFcmRepository = tokenFcmRepository;
        this.profilePresentationMapper = profilePresentationMapper;
        this.notificationService = notificationService;
    }

    /**
     * Retrieves basic personal information for the currently authenticated patient.
     *
     * @return a {@link GetProfileResponse} DTO containing basic patient details
     * @throws ApiException if the patient is not found or another domain exception occurs
     */
    public GetProfileResponse getBasicPatientInfo() {
        try {
            Patient patient = this.getPatientFromContext.get();

            // todo : test notification TO REMOVE
            String tokenFcm = this.tokenFcmRepository.get(patient.getId());
            NotificationMessage message = new NotificationMessage("Bienvenue sur l'application", "Ceci est une notification de test");
            this.notificationService.send(tokenFcm, message);

            return this.profilePresentationMapper.toDto(patient);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    @Override
    public void setTokenFcm(SaveTokenFcmRequest saveTokenFcmRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            this.tokenFcmRepository.save(patient.getId(), saveTokenFcmRequest.token());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
