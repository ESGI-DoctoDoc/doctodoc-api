package fr.esgi.doctodocapi.presentation.doctor.patient;

import fr.esgi.doctodocapi.dtos.responses.doctor.patient.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.use_cases.doctor.patient.GetDoctorPatients;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
public class GetPatientDetailController {
    private final GetDoctorPatients getDoctorPatients;

    public GetPatientDetailController(GetDoctorPatients getDoctorPatients) {
        this.getDoctorPatients = getDoctorPatients;
    }

    @GetMapping("patients")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetDoctorPatientResponse> getPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getDoctorPatients.execute(page, size);
    }
}
