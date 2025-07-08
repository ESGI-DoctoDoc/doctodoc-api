package fr.esgi.doctodocapi.presentation.patient.report_doctor;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.ReportDoctorRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.report_doctor.IReportDoctor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients/report-doctor")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class ReportDoctorController {
    private final IReportDoctor reportDoctor;

    public ReportDoctorController(IReportDoctor reportDoctor) {
        this.reportDoctor = reportDoctor;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void report(@Valid @RequestBody ReportDoctorRequest reportDoctorRequest) {
        this.reportDoctor.report(reportDoctorRequest);
    }
}
