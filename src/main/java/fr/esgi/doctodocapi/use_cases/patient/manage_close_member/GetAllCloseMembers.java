package fr.esgi.doctodocapi.use_cases.patient.manage_close_member;

import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllCloseMembers {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public GetAllCloseMembers(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
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
        closeMembers.forEach(closeMember -> closeMemberResponses.add(new GetCloseMemberResponse(closeMember.getId(), closeMember.getLastName(), closeMember.getFirstName(), closeMember.getEmail().getValue(), closeMember.getGender().name(), closeMember.getPhoneNumber().getValue(), closeMember.getBirthdate().getValue().toString(), false)));

        return closeMemberResponses;
    }
}
