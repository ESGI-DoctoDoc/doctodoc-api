package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorByIdResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_medical_concerns_response.GetAdminDoctorMedicalConcernsResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_report_response.GetReportResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorByIdForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorMedicalConcernsAndQuestions;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorReports;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorsForAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GetDoctorController {
    private final IGetDoctorByIdForAdmin getDoctorByIdForAdmin;
    private final IGetDoctorsForAdmin getDoctorsForAdmin;
    private final IGetDoctorMedicalConcernsAndQuestions getDoctorMedicalConcernsAndQuestions;
    private final IGetDoctorReports getDoctorReports;

    public GetDoctorController(IGetDoctorByIdForAdmin getDoctorByIdForAdmin, IGetDoctorsForAdmin getDoctorsForAdmin, IGetDoctorMedicalConcernsAndQuestions getDoctorMedicalConcernsAndQuestions, IGetDoctorReports getDoctorReports) {
        this.getDoctorByIdForAdmin = getDoctorByIdForAdmin;
        this.getDoctorsForAdmin = getDoctorsForAdmin;
        this.getDoctorMedicalConcernsAndQuestions = getDoctorMedicalConcernsAndQuestions;
        this.getDoctorReports = getDoctorReports;
    }

    @GetMapping("admin/doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorForAdminResponse> getDoctors(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return this.getDoctorsForAdmin.execute(page, size);
    }

    @GetMapping("admin/doctors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetDoctorByIdResponse execute(@PathVariable UUID id) {
        return this.getDoctorByIdForAdmin.execute(id);
    }


    @GetMapping("admin/doctors/{id}/reporting")
    @ResponseStatus(HttpStatus.OK)
    public List<GetReportResponse> getReporting(@PathVariable UUID id) {
        return this.getDoctorReports.get(id);
    }


    @GetMapping("admin/doctors/{id}/medical-concerns")
    @ResponseStatus(HttpStatus.OK)
    public List<GetAdminDoctorMedicalConcernsResponse> getMedicalConcernsAndQuestions(@PathVariable UUID id) {
        return this.getDoctorMedicalConcernsAndQuestions.get(id);
    }
}
