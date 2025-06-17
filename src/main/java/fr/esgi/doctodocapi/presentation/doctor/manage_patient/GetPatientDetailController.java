package fr.esgi.doctodocapi.presentation.doctor.manage_patient;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient.IGetDoctorPatients;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
public class GetPatientDetailController {
    private final IGetDoctorPatients getDoctorPatients;

    public GetPatientDetailController(IGetDoctorPatients getDoctorPatients) {
        this.getDoctorPatients = getDoctorPatients;
    }

    @GetMapping("patients")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetDoctorPatientResponse> getPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getDoctorPatients.execute(page, size);
    }
}
