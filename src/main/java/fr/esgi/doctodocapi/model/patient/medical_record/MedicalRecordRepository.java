package fr.esgi.doctodocapi.model.patient.medical_record;

import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.document.Document;

import java.util.List;
import java.util.UUID;

public interface MedicalRecordRepository {
    MedicalRecord getByPatientId(UUID patientId) throws MedicalConcernNotFoundException;
    void save(MedicalRecord medicalRecord);

    List<Document> getDocumentsByPatientId(UUID patientId, int page, int size) throws MedicalConcernNotFoundException;
}
