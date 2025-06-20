package fr.esgi.doctodocapi.model.care_tracking;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.patient.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CareTracking {
    UUID id;
    String caseName;
    String description;
    Doctor creator;
    Patient patient;
    List<String> documents;
    List<UUID> doctors;
    List<Appointment> appointments;
    List<CareTrackingTrace> careTrackingTraces;
    LocalDate createdAt;
    LocalDate closedAt;

    public CareTracking(UUID id, String caseName, String description, Doctor creator, Patient patient, List<String> documents, List<UUID> doctors, List<Appointment> appointments, List<CareTrackingTrace> careTrackingTraces, LocalDate createdAt, LocalDate closedAt) {
        this.id = id;
        this.caseName = caseName;
        this.description = description;
        this.creator = creator;
        this.patient = patient;
        this.documents = documents;
        this.doctors = doctors;
        this.appointments = appointments;
        this.careTrackingTraces = careTrackingTraces;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }

    public static CareTracking initialize(String caseName, String description, Doctor creator, Patient patient) {
        return new CareTracking(
                UUID.randomUUID(),
                caseName,
                description,
                creator,
                patient,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                LocalDate.now(),
                null
        );
    }

    public void addDocument(String document) {
        this.documents.add(document);
    }

    public void addDoctor(UUID doctorId) {
        this.doctors.add(doctorId);
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public void addTrace(CareTrackingTrace trace) {
        this.careTrackingTraces.add(trace);
    }

    public void closeTracking(LocalDate closedAt) {
        this.closedAt = closedAt;
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

    public Doctor getCreator() {
        return creator;
    }

    public void setCreator(Doctor creator) {
        this.creator = creator;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public List<UUID> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<UUID> doctors) {
        this.doctors = doctors;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<CareTrackingTrace> getCareTrackingTraces() {
        return careTrackingTraces;
    }

    public void setCareTrackingTraces(List<CareTrackingTrace> careTrackingTraces) {
        this.careTrackingTraces = careTrackingTraces;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDate closedAt) {
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
