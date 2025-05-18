package fr.esgi.doctodocapi.model.patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository {
    Optional<Patient> getByUserId(UUID userId);
    void save(Patient patient);
    boolean isExistMainAccount(UUID userId);
    List<Patient> getCloseMembers(UUID id);

    Patient getById(UUID id) throws PatientNotFoundException;
}
