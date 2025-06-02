package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.responses.GetBasicPatientInfo;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for retrieving patient-related information for the authenticated user.
 * <p>
 * Provides operations such as fetching the patient's basic profile data,
 * and listing any registered close members.
 * </p>
 */
@Service
public class GetInformations {
    private final PatientRepository patientRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param patientRepository     repository for accessing patient entities
     * @param getCurrentUserContext interface to access the currently authenticated user's context
     * @param userRepository        repository for accessing user entities
     */
    public GetInformations(PatientRepository patientRepository,
                           GetCurrentUserContext getCurrentUserContext,
                           UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves basic personal information for the currently authenticated patient.
     *
     * @return a {@link GetBasicPatientInfo} DTO containing basic patient details
     * @throws ApiException if the patient is not found or another domain exception occurs
     */
    public GetBasicPatientInfo getBasicPatientInfo() {
        try {
            String email = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(email);

            Patient patient = this.patientRepository.getByUserId(user.getId())
                    .orElseThrow(PatientNotFoundException::new);

            return new GetBasicPatientInfo(
                    patient.getId(),
                    patient.getEmail().getValue(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getPhoneNumber().getValue()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    /**
     * Retrieves a list of close members associated with the authenticated user.
     *
     * @return a list of {@link GetCloseMemberResponse} containing close member IDs and names
     */
    public List<GetCloseMemberResponse> getCloseMembers() {
        String email = this.getCurrentUserContext.getUsername();
        User user = this.userRepository.findByEmail(email);

        List<Patient> closeMembers = this.patientRepository.getCloseMembers(user.getId());

        List<GetCloseMemberResponse> closeMemberResponses = new ArrayList<>();
        closeMembers.forEach(closeMember -> {
            String fullName = closeMember.getFirstName() + " " + closeMember.getLastName();
            closeMemberResponses.add(new GetCloseMemberResponse(closeMember.getId(), fullName, false));
        });

        return closeMemberResponses;
    }
}
