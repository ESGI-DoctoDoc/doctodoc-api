package fr.esgi.doctodocapi.presentation.patient.manage_referent_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveReferentDoctorRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor.IGetReferentDoctor;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor.ISetReferentDoctor;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients/referent-doctor")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class ManageReferentDoctorController {
    private final IGetReferentDoctor getReferentDoctor;
    private final ISetReferentDoctor addReferentDoctor;

    public ManageReferentDoctorController(IGetReferentDoctor getReferentDoctor, ISetReferentDoctor addReferentDoctor) {
        this.getReferentDoctor = getReferentDoctor;
        this.addReferentDoctor = addReferentDoctor;
    }

    @GetMapping()
    public GetSearchDoctorResponse get() {
        return this.getReferentDoctor.process();
    }

    @PostMapping()
    public GetSearchDoctorResponse set(@Valid @RequestBody SaveReferentDoctorRequest saveReferentDoctorRequest) {
        return this.addReferentDoctor.process(saveReferentDoctorRequest);
    }
}
