package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.appointment.exceptions.CannotBookAppointmentException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Appointment {
    private UUID id;
    private Slot slot;
    private Patient patient;
    private Doctor doctor;
    private MedicalConcern medicalConcern;
    private LocalDate date;
    private HoursRange hoursRange;
    private LocalDateTime takenAt;
    private AppointmentStatus status;
    private List<PreAppointmentAnswers> preAppointmentAnswers;

    public Appointment(UUID id, Slot slot, Patient patient, Doctor doctor, MedicalConcern medicalConcern, LocalTime startHour, LocalTime endHour, LocalDateTime takenAt, AppointmentStatus status, List<PreAppointmentAnswers> answers) {
        this.id = id;
        this.slot = slot;
        this.patient = patient;
        this.doctor = doctor;
        this.medicalConcern = medicalConcern;
        this.date = slot.getDate();
        this.hoursRange = HoursRange.of(startHour, endHour);
        this.takenAt = takenAt;
        this.status = status;
        this.preAppointmentAnswers = answers;
    }

    public static Appointment init(Slot slot, Patient patient, Doctor doctor, MedicalConcern medicalConcern, LocalTime starHour, List<PreAppointmentAnswers> answers) {
        HoursRange appointmentHoursRange = HoursRange.of(starHour, starHour.plusMinutes(medicalConcern.getDurationInMinutes().getValue()));
        verifyIfConflicts(slot, appointmentHoursRange);

        return new Appointment(
                UUID.randomUUID(),
                slot,
                patient,
                doctor,
                medicalConcern,
                appointmentHoursRange.getStart(),
                appointmentHoursRange.getEnd(),
                LocalDateTime.now(),
                AppointmentStatus.LOCKED,
                answers
        );
    }

    private static void verifyIfConflicts(Slot slot, HoursRange appointementToSaveHoursRange) {
        List<Appointment> appointmentsOfSlot = slot.getAppointments();
        boolean hasConflict = appointmentsOfSlot.stream().anyMatch(appointment -> HoursRange.isTimesOverlap(appointementToSaveHoursRange, appointment.getHoursRange()));
        if (hasConflict) {
            throw new CannotBookAppointmentException();
        }
    }

    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<PreAppointmentAnswers> getPreAppointmentAnswers() {
        return preAppointmentAnswers;
    }

    public void setPreAppointmentAnswers(List<PreAppointmentAnswers> preAppointmentAnswers) {
        this.preAppointmentAnswers = preAppointmentAnswers;
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
