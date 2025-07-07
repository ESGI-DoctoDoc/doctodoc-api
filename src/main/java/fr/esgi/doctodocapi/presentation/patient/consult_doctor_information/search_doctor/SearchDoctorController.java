package fr.esgi.doctodocapi.presentation.patient.consult_doctor_information.search_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetSearchDoctorResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.consult_doctor_information.search_doctor.ISearchDoctor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_PATIENT')")
@RequestMapping("/patients/doctors")
public class SearchDoctorController {
    private final ISearchDoctor searchDoctor;

    public SearchDoctorController(ISearchDoctor searchDoctor) {
        this.searchDoctor = searchDoctor;
    }

    @GetMapping(value = "/search")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetSearchDoctorResponse> search(@RequestParam(defaultValue = "") String name,
                                                @RequestParam(defaultValue = "") String speciality,
                                                @RequestParam(defaultValue = "") List<String> languages,
                                                @RequestParam(defaultValue = "true") boolean valid,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size
    ) {
        return this.searchDoctor.process(name, speciality, languages, valid, page, size);
    }
}
