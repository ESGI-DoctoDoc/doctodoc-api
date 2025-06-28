package fr.esgi.doctodocapi.infrastructure.services.admin;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.use_cases.admin.ports.out.ManageDoctorValidationAccount;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService implements ManageDoctorValidationAccount {

    private final DoctorRepository doctorRepository;

    public AdminService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void validateDoctorAccount(UUID doctorId) {
        try {
            Doctor doctor = this.doctorRepository.findDoctorByUserId(doctorId);

            if (doctor == null) {
                throw new DoctorNotFoundException();
            }

            doctor.validate();
            this.doctorRepository.save(doctor);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getCode(), e.getMessage());
        }
    }

    public void refuseDoctorAccount(UUID doctorId) {
        try {
            Doctor doctor = this.doctorRepository.findDoctorByUserId(doctorId);

            if (doctor == null) {
                throw new DoctorNotFoundException();
            }

            doctor.refuse();
            this.doctorRepository.save(doctor);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getCode(), e.getMessage());
        }
    }
}
