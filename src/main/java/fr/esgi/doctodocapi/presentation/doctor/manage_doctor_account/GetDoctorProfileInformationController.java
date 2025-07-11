package fr.esgi.doctodocapi.presentation.doctor.manage_doctor_account;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.UpdateDoctorProfileRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.GetDoctorProfileInformationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.GetUpdatedProfileResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IGetDoctorProfileInformation;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IUpdateDoctorProfile;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class GetDoctorProfileInformationController {
    private final IGetDoctorProfileInformation doctorProfileResponse;
    private final IUpdateDoctorProfile updateDoctorProfile;

    public GetDoctorProfileInformationController(IGetDoctorProfileInformation doctorProfileResponse, IUpdateDoctorProfile updateDoctorProfile) {
        this.doctorProfileResponse = doctorProfileResponse;
        this.updateDoctorProfile = updateDoctorProfile;
    }

    @GetMapping("profile")
    @ResponseStatus(HttpStatus.OK)
    public GetDoctorProfileInformationResponse getDoctorProfileResponse() {
        return this.doctorProfileResponse.execute();
    }

    @PatchMapping("profile")
    @ResponseStatus(HttpStatus.OK)
    public GetUpdatedProfileResponse update(@Valid @RequestBody UpdateDoctorProfileRequest request) {
        System.out.println(request);
        return this.updateDoctorProfile.execute(request);
    }
}
