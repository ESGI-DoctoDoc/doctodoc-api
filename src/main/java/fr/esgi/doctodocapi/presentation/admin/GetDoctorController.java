package fr.esgi.doctodocapi.presentation.admin;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorByIdResponse;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.GetDoctorForAdminResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorByIdForAdmin;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.get_doctor.IGetDoctorsForAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GetDoctorController {
    private final IGetDoctorByIdForAdmin getDoctorByIdForAdmin;
    private final IGetDoctorsForAdmin getDoctorsForAdmin;

    public GetDoctorController(IGetDoctorByIdForAdmin getDoctorByIdForAdmin, IGetDoctorsForAdmin getDoctorsForAdmin) {
        this.getDoctorByIdForAdmin = getDoctorByIdForAdmin;
        this.getDoctorsForAdmin = getDoctorsForAdmin;
    }

    @GetMapping("doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDoctorForAdminResponse> getDoctors(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return this.getDoctorsForAdmin.execute(page, size);
    }

    @GetMapping("doctors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetDoctorByIdResponse execute(@PathVariable UUID id) {
        return this.getDoctorByIdForAdmin.execute(id);
    }
}
