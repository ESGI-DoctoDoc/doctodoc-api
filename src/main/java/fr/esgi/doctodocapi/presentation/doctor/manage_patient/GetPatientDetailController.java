package fr.esgi.doctodocapi.presentation.doctor.manage_patient;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientDetailResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_patient.GetDoctorPatientResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient.IGetDoctorPatients;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_patient.IGetPatientDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class GetPatientDetailController {
    private final IGetDoctorPatients getDoctorPatients;
    private final IGetPatientDetails getPatientDetails;

    public GetPatientDetailController(IGetDoctorPatients getDoctorPatients, IGetPatientDetails getPatientDetails) {
        this.getDoctorPatients = getDoctorPatients;
        this.getPatientDetails = getPatientDetails;
    }

    @GetMapping("patients")
    @ResponseStatus(value = HttpStatus.OK)
    public List<GetDoctorPatientResponse> getPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.getDoctorPatients.execute(page, size);
    }

    @GetMapping("patients/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetDoctorPatientDetailResponse getPatientDetails(@PathVariable UUID id) {
        return this.getPatientDetails.execute(id);
    }
}
