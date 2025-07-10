package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.AtLeastOneMedicalConcernException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.UpdateSlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetUpdatedSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.IUpdateSlot;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdateSlot implements IUpdateSlot {
    private final GetCurrentUserContext currentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final SlotRepository slotRepository;
    private final MedicalConcernRepository medicalConcernRepository;

    public UpdateSlot(GetCurrentUserContext currentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository) {
        this.currentUserContext = currentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
    }

    public GetUpdatedSlotResponse execute(UUID slotId, UpdateSlotRequest request) {
        String username = this.currentUserContext.getUsername();
        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Slot originalSlot = this.slotRepository.getById(slotId);
            LocalTime newStart = LocalTime.parse(request.startHour());
            LocalTime newEnd = LocalTime.parse(request.endHour());
            HoursRange newRange = HoursRange.of(newStart, newEnd);

            if (request.medicalConcerns().isEmpty()) {
                throw new AtLeastOneMedicalConcernException();
            }

            List<MedicalConcern> newConcerns = this.medicalConcernRepository.findAllById(request.medicalConcerns());

            originalSlot.update(newRange, newConcerns);
            List<Slot> slotsSameDay = this.slotRepository.findAllByDoctorIdAndDate(doctor.getId(), originalSlot.getDate());
            originalSlot.validateAgainstOverlaps(slotsSameDay.stream()
                    .filter(s -> !s.getId().equals(originalSlot.getId()))
                    .toList());

            Slot savedSlot = this.slotRepository.update(originalSlot);

            return new GetUpdatedSlotResponse(
                    savedSlot.getId(),
                    savedSlot.getHoursRange().getStart().toString(),
                    savedSlot.getHoursRange().getEnd().toString(),
                    savedSlot.getAvailableMedicalConcerns().stream()
                            .map(mc -> new GetUpdatedSlotResponse.MedicalConcernUpdateResponse(mc.getId(), mc.getName()))
                            .toList()
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public List<GetUpdatedSlotResponse> executeAllFromRecurrence(UUID slotId, UpdateSlotRequest request) {
        String username = this.currentUserContext.getUsername();
        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            Slot originalSlot = this.slotRepository.getById(slotId);
            UUID recurrenceId = originalSlot.getRecurrenceId();

            LocalTime newStart = LocalTime.parse(request.startHour());
            LocalTime newEnd = LocalTime.parse(request.endHour());
            HoursRange newRange = HoursRange.of(newStart, newEnd);

            if (request.medicalConcerns().isEmpty()) {
                throw new AtLeastOneMedicalConcernException();
            }
            List<MedicalConcern> newConcerns = this.medicalConcernRepository.findAllById(request.medicalConcerns());

            List<Slot> recurrentSlots = this.slotRepository.findAllByDoctorIdAndDateGreaterThanEqual(doctor.getId(), originalSlot.getDate()).stream()
                    .filter(s -> recurrenceId.equals(s.getRecurrenceId()))
                    .toList();

            List<GetUpdatedSlotResponse> responses = new ArrayList<>();

            for (Slot slot : recurrentSlots) {
                if (slot.getDate().isBefore(LocalDate.now())) {
                    continue;
                }
                slot.update(newRange, newConcerns);

                List<Slot> sameDaySlots = this.slotRepository.findAllByDoctorIdAndDate(doctor.getId(), slot.getDate()).stream()
                        .filter(s -> !s.getId().equals(slot.getId()))
                        .toList();

                slot.validateAgainstOverlaps(sameDaySlots);

                Slot updatedSlot = this.slotRepository.update(slot);

                responses.add(new GetUpdatedSlotResponse(
                        updatedSlot.getId(),
                        updatedSlot.getHoursRange().getStart().toString(),
                        updatedSlot.getHoursRange().getEnd().toString(),
                        updatedSlot.getAvailableMedicalConcerns().stream()
                                .map(mc -> new GetUpdatedSlotResponse.MedicalConcernUpdateResponse(mc.getId(), mc.getName()))
                                .toList()
                ));
            }

            return responses;

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
