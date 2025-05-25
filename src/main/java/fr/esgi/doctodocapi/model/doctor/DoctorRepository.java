package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;

import java.util.Optional;
import java.util.UUID;


public interface DoctorRepository {
    void save(Doctor doctor);
    Doctor findDoctorByUserId(UUID doctorId);

    Doctor getById(UUID id) throws DoctorNotFoundException;
    boolean isExistsById(UUID doctorId);
    Optional<Doctor> getByUserId(UUID id);
}
