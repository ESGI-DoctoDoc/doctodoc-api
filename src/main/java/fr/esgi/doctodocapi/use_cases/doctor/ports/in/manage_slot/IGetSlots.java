package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotByIdResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IGetSlots {
    List<GetSlotResponse> getAll(int page, int size, LocalDate startDate);
    GetSlotByIdResponse getSlotById(UUID slotId);
}
