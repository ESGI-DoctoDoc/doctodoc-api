package fr.esgi.doctodocapi.use_cases.admin.ports.in;

import java.util.UUID;

public interface IManageValidationDoctorAccount {
    void validateDoctorAccount(UUID doctorId);
    void refuseDoctorAccount(UUID doctorId);
}
