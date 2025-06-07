package fr.esgi.doctodocapi.model.doctor.calendar.slot;


import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.use_cases.appointment.MedicalConcernNotAuthorizedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Slot {
    private UUID id;
    private LocalDate date;
    private HoursRange hoursRange;
    private RecurrenceType recurrenceType;
    private UUID doctorId;
    private List<Appointment> appointments;
    private List<MedicalConcern> availableMedicalConcerns;

    // basic info
    public Slot(UUID id, LocalDate date, LocalTime startHour, LocalTime endHour) {
        this.id = id;
        this.date = date;
        this.hoursRange = HoursRange.of(startHour, endHour);
    }

    public Slot(UUID id, LocalDate date, LocalTime startHour, LocalTime endHour, List<Appointment> appointments, List<MedicalConcern> availableMedicalConcerns, UUID doctorId) {
        this.id = id;
        this.date = date;
        this.hoursRange = HoursRange.of(startHour, endHour);
        this.appointments = appointments;
        this.availableMedicalConcerns = availableMedicalConcerns;
        this.doctorId = doctorId;
    }

    public void validateIfSlotIsAuthorized(MedicalConcern medicalConcern) {
        if (!this.getAvailableMedicalConcerns().contains(medicalConcern)) {
            throw new MedicalConcernNotAuthorizedException();
        }
    }

    public static Slot create(LocalDate date, LocalTime startHour, LocalTime endHour, UUID doctorId, List<MedicalConcern> medicalConcerns) {
        return new Slot(
                UUID.randomUUID(),
                date,
                startHour,
                endHour,
                new ArrayList<>(),
                medicalConcerns,
                doctorId
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<MedicalConcern> getAvailableMedicalConcerns() {
        return availableMedicalConcerns;
    }

    public void setAvailableMedicalConcerns(List<MedicalConcern> availableMedicalConcerns) {
        this.availableMedicalConcerns = availableMedicalConcerns;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(id, slot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
