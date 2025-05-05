package fr.esgi.doctodocapi.model.patient;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository {
    Optional<Patient> getByUserId(UUID userId);
    void save(Patient patient);

    boolean isExistMainAccount(UUID userId);
}
