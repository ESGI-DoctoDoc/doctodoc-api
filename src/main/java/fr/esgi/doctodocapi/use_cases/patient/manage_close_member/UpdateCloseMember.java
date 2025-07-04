package fr.esgi.doctodocapi.use_cases.patient.manage_close_member;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveCloseMemberRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_close_member.IUpdateCloseMember;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;


public class UpdateCloseMember implements IUpdateCloseMember {
    private final PatientRepository patientRepository;

    public UpdateCloseMember(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public GetCloseMemberResponse process(UUID closeMemberId, SaveCloseMemberRequest saveCloseMemberRequest) {
        String firstName = saveCloseMemberRequest.firstName();
        String lastName = saveCloseMemberRequest.lastName();
        String email = saveCloseMemberRequest.email();
        String gender = saveCloseMemberRequest.gender();
        String phoneNumber = saveCloseMemberRequest.phoneNumber();
        LocalDate birthdate = saveCloseMemberRequest.birthdate();

        try {

            Patient patient = this.patientRepository.getById(closeMemberId);
            patient.update(firstName, lastName, gender, email, phoneNumber, birthdate);
            Patient closeMemberSaved = this.patientRepository.update(patient);
            return new GetCloseMemberResponse(closeMemberSaved.getId(), closeMemberSaved.getLastName(), closeMemberSaved.getFirstName(), closeMemberSaved.getEmail().getValue(), closeMemberSaved.getGender().name(), closeMemberSaved.getPhoneNumber().getValue(), closeMemberSaved.getBirthdate().getValue().toString(), false);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }
}
