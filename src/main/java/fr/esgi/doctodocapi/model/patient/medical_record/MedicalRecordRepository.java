package fr.esgi.doctodocapi.model.patient.medical_record;

import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;

import java.util.UUID;

public interface MedicalRecordRepository {
    MedicalRecord getByPatientId(UUID patientId) throws MedicalConcernNotFoundException;

    void save(MedicalRecord medicalRecord);
}
