package fr.esgi.doctodocapi.use_cases.admin.ports.out;

import java.util.UUID;

public interface ManageDoctorValidationAccount {
    void validateDoctorAccount(UUID doctorId);
    void refuseDoctorAccount(UUID doctorId);
}
