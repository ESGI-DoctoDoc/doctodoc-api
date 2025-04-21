package fr.esgi.doctodocapi.model.patient;

import java.util.UUID;

public interface PatientRepository {
    boolean isExistPatientByUserId(UUID userId);

}
