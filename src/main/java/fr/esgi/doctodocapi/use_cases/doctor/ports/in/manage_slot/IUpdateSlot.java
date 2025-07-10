package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.UpdateSlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetUpdatedSlotResponse;

import java.util.UUID;

public interface IUpdateSlot {
    GetUpdatedSlotResponse execute(UUID slotId, UpdateSlotRequest request);
}
