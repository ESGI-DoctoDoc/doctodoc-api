package fr.esgi.doctodocapi.presentation.patient.manage_close_member;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveCloseMemberRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_close_member.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class ManageCloseMemberController {
    private final IGetAllCloseMembers getAllCloseMembers;
    private final IGetCloseMember getCloseMember;
    private final ICreateCloseMember createCloseMember;
    private final IUpdateCloseMember updateCloseMember;
    private final IDeleteCloseMember deleteCloseMember;

    public ManageCloseMemberController(IGetAllCloseMembers getAllCloseMembers, IGetCloseMember getCloseMember, ICreateCloseMember createCloseMember, IUpdateCloseMember updateCloseMember, IDeleteCloseMember deleteCloseMember) {
        this.getAllCloseMembers = getAllCloseMembers;
        this.getCloseMember = getCloseMember;
        this.createCloseMember = createCloseMember;
        this.updateCloseMember = updateCloseMember;
        this.deleteCloseMember = deleteCloseMember;
    }

    /**
     * Returns a list of close members related to the patient.
     *
     * @return list of close member DTOs
     */
    @GetMapping("patients/close-members")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetCloseMemberResponse> getCloseMembers() {
        return this.getAllCloseMembers.getCloseMembers();
    }

    @GetMapping("patients/close-members/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public GetCloseMemberResponse getCloseMember(@PathVariable UUID id) {
        return this.getCloseMember.get(id);
    }

    @PostMapping("/patients/close-members")
    @ResponseStatus(HttpStatus.CREATED)
    public GetCloseMemberResponse createCloseMember(@Valid @RequestBody SaveCloseMemberRequest saveCloseMemberRequest) {
        return this.createCloseMember.process(saveCloseMemberRequest);
    }

    @PutMapping("/patients/close-members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetCloseMemberResponse updateCloseMember(@PathVariable UUID id, @Valid @RequestBody SaveCloseMemberRequest saveCloseMemberRequest) {
        return this.updateCloseMember.process(id, saveCloseMemberRequest);
    }

    @DeleteMapping("/patients/close-members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCloseMember(@PathVariable UUID id) {
        this.deleteCloseMember.process(id);
    }
}
