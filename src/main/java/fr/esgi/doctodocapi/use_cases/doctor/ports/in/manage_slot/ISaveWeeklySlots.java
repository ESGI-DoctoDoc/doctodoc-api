package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.WeeklySlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;

import java.util.List;

public interface ISaveWeeklySlots {
    List<GetSlotResponse> execute(WeeklySlotRequest request);
}
