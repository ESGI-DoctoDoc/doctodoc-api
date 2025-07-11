package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.appointment.exceptions.CannotBookAppointmentException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static fr.esgi.doctodocapi.model.appointment.AppointmentsAvailabilityService.LOCKED_MAX_TIME_IN_MINUTE;

public class Appointment {
    private UUID id;
    private Slot slot;
    private Patient patient;
    private Doctor doctor;
    private MedicalConcern medicalConcern;
    private LocalDate date;
    private HoursRange hoursRange;
    private LocalDateTime takenAt;
    private LocalDateTime lockedAt;
    private AppointmentStatus status;
    private List<PreAppointmentAnswers> preAppointmentAnswers;
    private String doctorNotes;
    private String cancelExplanation;
    private UUID careTrackingId;

    public Appointment(UUID id, Slot slot, Patient patient, Doctor doctor, MedicalConcern medicalConcern, LocalTime startHour, LocalTime endHour, LocalDateTime takenAt, AppointmentStatus status, List<PreAppointmentAnswers> answers, LocalDateTime lockedAt, String cancelExplanation, String doctorNotes, UUID careTrackingId) {
        this.id = id;
        this.slot = slot;
        this.patient = patient;
        this.doctor = doctor;
        this.medicalConcern = medicalConcern;
        this.date = slot.getDate();
        this.hoursRange = HoursRange.of(startHour, endHour);
        this.takenAt = takenAt;
        this.lockedAt = lockedAt;
        this.status = status;
        this.preAppointmentAnswers = answers;
        this.doctorNotes = doctorNotes;
        this.cancelExplanation = cancelExplanation;
        this.careTrackingId = careTrackingId;
    }

    public Appointment(UUID id, LocalTime startHour, LocalTime endHour, LocalDateTime takenAt, AppointmentStatus status, List<PreAppointmentAnswers> answers, LocalDateTime lockedAt, String cancelExplanation) {
        this.id = id;
        this.hoursRange = HoursRange.of(startHour, endHour);
        this.takenAt = takenAt;
        this.lockedAt = lockedAt;
        this.status = status;
        this.preAppointmentAnswers = answers;
        this.cancelExplanation = cancelExplanation;
    }

    /**
     * Initializes a new appointment, ensuring no conflicts with existing appointments in the slot.
     *
     * @param slot           the time slot to book the appointment in
     * @param patient        the patient booking the appointment
     * @param doctor         the doctor assigned to the appointment
     * @param medicalConcern the medical concern for this appointment
     * @param startHour      the desired start time of the appointment
     * @param answers        pre-appointment questionnaire answers
     * @return a new Appointment instance locked for booking
     * @throws CannotBookAppointmentException if the new appointment overlaps with an existing one
     */
    public static Appointment init(Slot slot, Patient patient, Doctor doctor, MedicalConcern medicalConcern, LocalTime startHour, List<PreAppointmentAnswers> answers, UUID careTrackingId) {
        HoursRange appointmentHoursRange = HoursRange.of(startHour, startHour.plusMinutes(medicalConcern.getDurationInMinutes().getValue()));
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
                answers,
                LocalDateTime.now(),
                null,
                null,
                careTrackingId
        );
    }

    public static Appointment initFromDoctor(Slot slot, Patient patient, Doctor doctor, MedicalConcern medicalConcern, LocalTime startHour, List<PreAppointmentAnswers> answers, String doctorNotes, UUID careTrackingId) {
        HoursRange appointmentHoursRange = HoursRange.of(startHour, startHour.plusMinutes(medicalConcern.getDurationInMinutes().getValue()));
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
                AppointmentStatus.CONFIRMED,
                answers,
                null,
                null,
                doctorNotes,
                careTrackingId
        );
    }

    /**
     * Checks if the given appointment hours range conflicts with any existing appointments in the slot.
     *
     * @param slot                        the slot to check for conflicts
     * @param appointmentToSaveHoursRange the proposed hours range for the new appointment
     * @throws CannotBookAppointmentException if a conflict is detected
     */
    private static void verifyIfConflicts(Slot slot, HoursRange appointmentToSaveHoursRange) {
        List<Appointment> appointmentsOfSlot = slot.getAppointments().stream().filter(appointment ->
                appointment.getStatus() == AppointmentStatus.CONFIRMED || !appointment.isLockExpired()
        ).toList();
        boolean hasConflict = appointmentsOfSlot.stream().anyMatch(appointment -> HoursRange.isTimesOverlap(appointmentToSaveHoursRange, appointment.getHoursRange()));
        if (hasConflict) {
            throw new CannotBookAppointmentException();
        }
    }

    private boolean isLockExpired() {
        if (this.status == AppointmentStatus.LOCKED && this.getLockedAt() != null) {
            LocalDateTime unlockedTime = this.getLockedAt().plusMinutes(LOCKED_MAX_TIME_IN_MINUTE);
            LocalDateTime now = LocalDateTime.now();
            return unlockedTime.isBefore(now);
        }
        return true;
    }

    /**
     * Confirms the appointment by changing its status to CONFIRMED.
     */
    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
        this.lockedAt = null;
    }

    public void completed() {
        this.status = AppointmentStatus.COMPLETED;
    }

    /**
     * Confirms the appointment by changing its status to CANCELLED.
     */
    public void cancel(String cancelExplanation) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancelExplanation = cancelExplanation;
    }

    public void update(Slot newSlot, MedicalConcern newMedicalConcern, LocalDate newDate, LocalTime newStartHour, List<PreAppointmentAnswers> newAnswers, String newDoctorNotes, UUID newCareTrackingId) {
        HoursRange newRange = HoursRange.of(
                newStartHour,
                newStartHour.plusMinutes(newMedicalConcern.getDurationInMinutes().getValue())
        );

        List<Appointment> otherAppointments = newSlot.getAppointments().stream()
                .filter(app -> !app.getId().equals(this.id))
                .filter(app -> app.getStatus() == AppointmentStatus.CONFIRMED || !app.isLockExpired())
                .toList();

        boolean hasConflict = otherAppointments.stream()
                .anyMatch(existing -> HoursRange.isTimesOverlap(existing.getHoursRange(), newRange));

        if (hasConflict) {
            throw new CannotBookAppointmentException();
        }

        this.slot = newSlot;
        this.medicalConcern = newMedicalConcern;
        this.date = newDate;
        this.hoursRange = newRange;
        this.preAppointmentAnswers = newAnswers;
        this.doctorNotes = newDoctorNotes;
        this.careTrackingId = newCareTrackingId;
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

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public void setPreAppointmentAnswers(List<PreAppointmentAnswers> preAppointmentAnswers) {
        this.preAppointmentAnswers = preAppointmentAnswers;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public UUID getCareTrackingId() {
        return careTrackingId;
    }

    public void setCareTrackingId(UUID careTrackingId) {
        this.careTrackingId = careTrackingId;
    }

    public String getCancelExplanation() {
        return cancelExplanation;
    }

    public void setCancelExplanation(String cancelExplanation) {
        this.cancelExplanation = cancelExplanation;
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
