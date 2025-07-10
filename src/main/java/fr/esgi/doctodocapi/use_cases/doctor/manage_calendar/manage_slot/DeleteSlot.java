package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.DeleteSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.IDeleteSlot;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

//    public List<DeleteSlotResponse> executeAllFromRecurrence(UUID slotId) {
//        try {
//            Slot originalSlot = this.slotRepository.getById(slotId);
//
//            if (originalSlot.getRecurrenceId() == null) {
//                throw new ApiException(HttpStatus.BAD_REQUEST, "slot.not-recurrent", "Ce créneau n'est pas associé à une récurrence.");
//            }
//
//            UUID recurrenceId = originalSlot.getRecurrenceId();
//            UUID doctorId = originalSlot.getDoctorId();
//
//            List<Slot> recurrentSlots = this.slotRepository.findAllByDoctorIdAndDateGreaterThanEqual(doctorId, originalSlot.getDate())
//                    .stream()
//                    .filter(s -> recurrenceId.equals(s.getRecurrenceId()))
//                    .filter(s -> !s.getDate().isBefore(LocalDate.now()))
//                    .toList();
//
//            List<DeleteSlotResponse> responses = new ArrayList<>();
//
//            for (Slot slot : recurrentSlots) {
//                slot.getAppointments().forEach(appointment -> {
//                    appointment.cancel("Le créneau a été annulé par le docteur.");
//                    this.appointmentRepository.cancel(appointment);
//                });
//
//                this.slotRepository.delete(slot.getId());
//                responses.add(new DeleteSlotResponse());
//            }
//
//            return responses;
//
//        } catch (DomainException e) {
//            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
//        }
//    }
}
