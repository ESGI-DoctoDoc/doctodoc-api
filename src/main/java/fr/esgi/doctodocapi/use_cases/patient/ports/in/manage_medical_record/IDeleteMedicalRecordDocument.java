package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record;

import java.util.UUID;

public interface IDeleteMedicalRecordDocument {
    void process(UUID id);
}
