package fr.esgi.doctodocapi.use_cases.admin.validate_account;

import fr.esgi.doctodocapi.dtos.requests.doctor.DoctorValidationRequest;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.model.admin.Admin;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service responsible for validating doctor accounts.
 * <p>
 * This service allows an admin to validate a doctor's registration in the system.
 * It uses the doctor's user ID to find the corresponding entity and mark it as validated.
 * </p>
 */
@Service
public class ValidateDoctorAccount {

    private final DoctorRepository doctorRepository;

    /**
     * Constructs the service with the required repository.
     *
     * @param doctorRepository the repository to access doctor data
     */
    public ValidateDoctorAccount(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Validates a doctor account based on the given request.
     * <p>
     * Retrieves the doctor by their user ID, validates the account, and persists the change.
     * If the doctor is not found, a {@link DoctorNotFoundException} is thrown.
     * If any domain exception occurs during validation, an {@link ApiException} is thrown.
     * </p>
     *
     * @param request the doctor validation request containing the doctor's user ID
     */
    public void validateDoctorAccount(DoctorValidationRequest request) {
        Doctor doctor = this.doctorRepository.findDoctorByUserId(request.doctorId());

        if (doctor == null) {
            throw new DoctorNotFoundException();
        }

        try {
            Admin.validateDoctorAccount(doctor);
            this.doctorRepository.save(doctor);
        } catch (DoctorNotFoundException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
