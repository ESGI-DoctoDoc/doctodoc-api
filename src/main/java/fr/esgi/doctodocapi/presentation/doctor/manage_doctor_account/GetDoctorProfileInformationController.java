package fr.esgi.doctodocapi.presentation.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.GetDoctorProfileInformationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IGetDoctorProfileInformation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class GetDoctorProfileInformationController {
    private final IGetDoctorProfileInformation doctorProfileResponse;

    public GetDoctorProfileInformationController(IGetDoctorProfileInformation doctorProfileResponse) {
        this.doctorProfileResponse = doctorProfileResponse;
    }

    @GetMapping("profile")
    @ResponseStatus(HttpStatus.OK)
    public GetDoctorProfileInformationResponse getDoctorProfileResponse() {
        return this.doctorProfileResponse.execute();
    }
}
