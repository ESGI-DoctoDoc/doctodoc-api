package fr.esgi.doctodocapi.use_cases.user;

import fr.esgi.doctodocapi.dtos.requests.doctor.DoctorValidationRequest;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.admin.Admin;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ValidateDoctorAccount {
    private final DoctorRepository doctorRepository;

    public ValidateDoctorAccount(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

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
