package fr.esgi.doctodocapi.use_cases.patient.manage_close_member;

import fr.esgi.doctodocapi.dtos.requests.patient.SaveCloseMemberRequest;
import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Use case for creating a close member patient linked to the currently authenticated user.
 * A close member is typically a relative or dependent of the main patient.
 */
@Service
public class CreateCloseMember {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public CreateCloseMember(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Processes the creation of a close member patient.
     * Retrieves the current user, builds a new patient instance, and saves it.
     * If a domain-level error occurs during creation, an ApiException is thrown.
     *
     * @param saveCloseMemberRequest The data required to create a close member
     * @throws ApiException if patient creation fails due to business rule violations
     */
    public GetCloseMemberResponse process(SaveCloseMemberRequest saveCloseMemberRequest) {
        String username = this.getCurrentUserContext.getUsername();

        String firstName = saveCloseMemberRequest.firstName();
        String lastName = saveCloseMemberRequest.lastName();
        String email = saveCloseMemberRequest.email();
        String sexe = saveCloseMemberRequest.gender();
        String phoneNumber = saveCloseMemberRequest.phoneNumber();
        LocalDate birthdate = saveCloseMemberRequest.birthdate();

        try {

            User user = this.userRepository.findByEmail(username);
            Patient patient = Patient.createCloseMember(user, firstName, lastName, birthdate, email, sexe, phoneNumber);
            Patient closeMemberSaved = this.patientRepository.save(patient);
            return new GetCloseMemberResponse(closeMemberSaved.getId(), closeMemberSaved.getLastName(), closeMemberSaved.getFirstName(), closeMemberSaved.getEmail().getValue(), closeMemberSaved.getGender().name(), closeMemberSaved.getPhoneNumber().getValue(), closeMemberSaved.getBirthdate().getValue().toString(), false);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }
}
