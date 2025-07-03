package fr.esgi.doctodocapi.model.doctor.care_tracking;


import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.patient.Patient;

import java.util.List;
import java.util.UUID;

public interface CareTrackingRepository {
    // Doctor
    UUID save(CareTracking careTracking);
    List<CareTracking> findAll(UUID doctorId, int page, int size);
    CareTracking getById(UUID id) throws CareTrackingNotFoundException;

    CareTracking getByIdAndPatient(UUID careTrackingId, Patient patient) throws CareTrackingNotFoundException;

    CareTracking getByIdAndDoctor(UUID careTrackingId, Doctor doctor) throws CareTrackingNotFoundException;

    // Patient
    List<CareTracking> findAllByPatientId(UUID patientId, int page, int size);

    List<Document> getDocumentsByCareTrackingIdAndType(UUID id, String type, int page, int size);

    List<Document> getDocumentsByPatientId(UUID id, int page, int size);
}