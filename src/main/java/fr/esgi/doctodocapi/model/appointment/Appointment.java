package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public class Appointment {
    private UUID id;
    private Slot slot;
    private Patient patient;
    private MedicalConcern medicalConcern;
    private LocalDate date;
    private HoursRange hoursRange;
    private LocalDateTime takenAt;
    private AppointmentStatus status;

    public Appointment(UUID id, Slot slot, Patient patient, MedicalConcern medicalConcern, LocalTime startHour, LocalTime endHour, LocalDateTime takenAt, AppointmentStatus status) {
        this.id = id;
        this.slot = slot;
        this.patient = patient;
        this.medicalConcern = medicalConcern;
        this.date = slot.getDate();
        this.hoursRange = HoursRange.of(startHour, endHour);
        this.takenAt = takenAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public MedicalConcern getMedicalConcern() {
        return medicalConcern;
    }

    public void setMedicalConcern(MedicalConcern medicalConcern) {
        this.medicalConcern = medicalConcern;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HoursRange getHoursRange() {
        return hoursRange;
    }

    public void setHoursRange(HoursRange hoursRange) {
        this.hoursRange = hoursRange;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
