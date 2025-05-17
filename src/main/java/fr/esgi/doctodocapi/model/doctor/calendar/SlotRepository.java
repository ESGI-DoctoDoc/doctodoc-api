package fr.esgi.doctodocapi.model.doctor.calendar;

import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SlotRepository {
    List<Slot> getSlotsByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date) throws MedicalConcernNotFoundException;
}
