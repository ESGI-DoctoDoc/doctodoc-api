package fr.esgi.doctodocapi.use_cases.admin.ports.in;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.DoctorValidationRequest;

public interface IValidateDoctorAccount {
    void validateDoctorAccount(DoctorValidationRequest request);
}
