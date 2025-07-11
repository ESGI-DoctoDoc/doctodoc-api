package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.DeleteSlotResponse;

import java.util.List;
import java.util.UUID;

public interface IDeleteSlot {
    DeleteSlotResponse execute(UUID slotId);
    List<DeleteSlotResponse> executeAllFromRecurrence(UUID slotId);
}
