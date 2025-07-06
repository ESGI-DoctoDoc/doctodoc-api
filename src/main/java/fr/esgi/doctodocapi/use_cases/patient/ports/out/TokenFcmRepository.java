package fr.esgi.doctodocapi.use_cases.patient.ports.out;

import java.util.UUID;

public interface TokenFcmRepository {
    void save(UUID patientId, String tokenFcm);

    String get(UUID patientId);

}
