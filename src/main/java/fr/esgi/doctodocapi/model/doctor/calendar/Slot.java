package fr.esgi.doctodocapi.model.doctor.calendar;


import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Slot {
    private UUID id;
    private LocalDate date;
    private HoursRange hoursRange;
    private List<MedicalConcern> availableMedicalConcerns;

    public Slot(UUID id, LocalDate date, LocalTime startHour, LocalTime endHour, List<MedicalConcern> availableMedicalConcerns) {
        this.id = id;
        this.date = date;
        this.hoursRange = HoursRange.of(startHour, endHour);
        this.availableMedicalConcerns = availableMedicalConcerns;
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
