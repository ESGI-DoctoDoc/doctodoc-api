package fr.esgi.doctodocapi.model.doctor.calendar;

import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SlotRepository {
    List<Slot> getSlotsByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date) throws MedicalConcernNotFoundException;

    Slot getById(UUID id) throws SlotNotFoundException;
}
