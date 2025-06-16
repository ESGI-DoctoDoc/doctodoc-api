package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;

import java.time.LocalDate;
import java.util.List;

public interface IGetAllSlots {
    List<GetSlotResponse> getAll(int page, int size, LocalDate startDate);
}
