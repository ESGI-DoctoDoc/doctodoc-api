package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.ExceptionalSlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;

public interface ISaveExceptionalSlot {
    GetSlotResponse execute(ExceptionalSlotRequest request);
}
