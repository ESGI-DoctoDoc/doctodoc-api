package fr.esgi.doctodocapi.model.doctor;

import java.util.UUID;


public interface DoctorRepository {
    boolean isExistByUserId(UUID userId);
    Doctor getById(UUID treatingDoctorId) throws DoctorNotFoundException;
}
