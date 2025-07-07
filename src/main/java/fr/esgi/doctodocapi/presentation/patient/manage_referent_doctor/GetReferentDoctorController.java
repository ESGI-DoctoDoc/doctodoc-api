package fr.esgi.doctodocapi.presentation.patient.manage_referent_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_referent_doctor.IGetReferentDoctor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients/referent-doctor")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetReferentDoctorController {
    private final IGetReferentDoctor getReferentDoctor;

    public GetReferentDoctorController(IGetReferentDoctor getReferentDoctor) {
        this.getReferentDoctor = getReferentDoctor;
    }

    @GetMapping()
    public GetSearchDoctorResponse get() {
        return this.getReferentDoctor.process();
    }
}
