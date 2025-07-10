package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.DeleteSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.IDeleteSlot;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DeleteSlot implements IDeleteSlot {
    private final SlotRepository slotRepository;
    private final AppointmentRepository appointmentRepository;

    public DeleteSlot(SlotRepository slotRepository, AppointmentRepository appointmentRepository) {
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public DeleteSlotResponse execute(UUID slotId) {
        try {
            Slot slot = this.slotRepository.getById(slotId);

            slot.getAppointments().forEach(appointment -> {
                appointment.cancel("Le créneau a été annulé par le docteur.");
                this.appointmentRepository.cancel(appointment);
            });

            this.slotRepository.delete(slot.getId());

            return new DeleteSlotResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
