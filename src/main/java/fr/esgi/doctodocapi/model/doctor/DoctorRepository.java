package fr.esgi.doctodocapi.model.doctor;

import java.util.Optional;
import java.util.UUID;


public interface DoctorRepository {
    Doctor getById(UUID treatingDoctorId) throws DoctorNotFoundException;

    Optional<Doctor> getByUserId(UUID id);
}
