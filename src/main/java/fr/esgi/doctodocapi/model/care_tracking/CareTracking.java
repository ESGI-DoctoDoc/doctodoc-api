package fr.esgi.doctodocapi.model.care_tracking;

import fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.medical_record.DocumentWithSameNameAlreadyExist;
import fr.esgi.doctodocapi.model.patient.Patient;

import java.time.LocalDateTime;
import java.util.*;

public class CareTracking {
    private UUID id;
    private String caseName;
    private String description;
    private UUID creatorId;
    private Patient patient;
    private List<CareTrackingDocument> documents;
    private List<UUID> doctors;
    private List<UUID> appointmentsId;
    private List<CareTrackingTrace> careTrackingTraces;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public CareTracking(UUID id, String caseName, String description, UUID creatorId, Patient patient,
                        List<CareTrackingDocument> documents, List<UUID> doctors, List<UUID> appointmentsId,
                        List<CareTrackingTrace> careTrackingTraces, LocalDateTime createdAt, LocalDateTime closedAt) {
        this.id = id;
        this.caseName = caseName;
        this.description = description;
        this.creatorId = creatorId;
        this.patient = patient;
        this.documents = new ArrayList<>(documents);
        this.doctors = new ArrayList<>(doctors);
        this.appointmentsId = new ArrayList<>(appointmentsId);
        this.careTrackingTraces = new ArrayList<>(careTrackingTraces);
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }

    public static CareTracking initialize(String caseName, String description, UUID creatorId, Patient patient) {
        return new CareTracking(
                UUID.randomUUID(),
                caseName,
                description,
                creatorId,
                patient,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                LocalDateTime.now(),
                null
        );
    }

    public CareTrackingDocument getById(UUID id) {
        return this.documents
                .stream()
                .filter(document -> document.getDocument().getId().equals(id))
                .findFirst()
                .orElseThrow(DocumentNotFoundInCareTrackingException::new);
    }

    public void addDocument(CareTrackingDocument careTrackingDocument) {
        if (documents.stream().anyMatch(d -> d.getDocument().getName().equalsIgnoreCase(careTrackingDocument.getDocument().getName()))) {
            throw new DocumentAlreadyExistInCareTrackingException();
        }
        documents.add(careTrackingDocument);
    }

    public void updateDocument(CareTrackingDocument oldDocument, CareTrackingDocument newDocument) {
        verifyIfFilenameAlreadyExist(newDocument.getDocument().getName(), oldDocument);
        if (!this.documents.contains(oldDocument)) throw new DocumentNotFoundException();
        this.documents.remove(oldDocument);
        this.documents.add(newDocument);
    }

//    public void addDoctor(UUID doctorId) {
//        verifyIfNotClosed();
//        if (doctors.contains(doctorId)) {
//            throw new DoctorAlreadyExistInCareTrackingException();
//        }
//        doctors.add(doctorId);
//    }

    public void addAppointment(UUID appointmentId) {
        verifyIfNotClosed();
        if (appointmentsId.contains(appointmentId)) {
            throw new AppointmentAlreadyExistInCareTrackingException();
        }
        appointmentsId.add(appointmentId);
    }

    public void addTrace(CareTrackingTrace trace) {
        if (careTrackingTraces.contains(trace)) {
            throw new TraceAlreadyExistInCareTrackingException();
        }
        careTrackingTraces.add(trace);
    }

    private void verifyIfNotClosed() {
        if (this.closedAt != null) {
            throw new ClosedCareTrackingException();
        }
    }

    private void verifyIfFilenameAlreadyExist(String filename, CareTrackingDocument excludedDocument) {
        boolean exists = this.documents.stream()
                .filter(doc -> !doc.equals(excludedDocument))
                .anyMatch(doc -> Objects.equals(doc.getDocument().getName(), filename));

        if (exists) {
            throw new DocumentWithSameNameAlreadyExist();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<CareTrackingDocument> getDocuments() {
        // todo doctor needs permission too
        return Collections.unmodifiableList(documents);
    }

    public void setDocuments(List<CareTrackingDocument> documents) {
        this.documents = documents;
    }

    public List<UUID> getDoctors() {
        return Collections.unmodifiableList(doctors);
    }

    public void setDoctors(List<UUID> doctors) {
        this.doctors = doctors;
    }

    public List<UUID> getAppointmentsId() {
        return Collections.unmodifiableList(appointmentsId);
    }

    public void setAppointmentsId(List<UUID> appointmentsId) {
        this.appointmentsId = appointmentsId;
    }

    public List<CareTrackingTrace> getCareTrackingTraces() {
        return Collections.unmodifiableList(careTrackingTraces);
    }

    public void setCareTrackingTraces(List<CareTrackingTrace> careTrackingTraces) {
        this.careTrackingTraces = careTrackingTraces;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CareTracking that = (CareTracking) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
