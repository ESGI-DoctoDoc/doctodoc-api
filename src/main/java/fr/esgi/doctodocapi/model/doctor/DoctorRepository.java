package fr.esgi.doctodocapi.model.doctor;

import java.util.Optional;
import java.util.UUID;


public interface DoctorRepository {
    void save(Doctor doctor);
    Doctor findDoctorByUserId(UUID doctorId);
    Doctor getById(UUID treatingDoctorId) throws DoctorNotFoundException;
    boolean isExistsById(UUID doctorId);

    Optional<Doctor> getByUserId(UUID id);
}
