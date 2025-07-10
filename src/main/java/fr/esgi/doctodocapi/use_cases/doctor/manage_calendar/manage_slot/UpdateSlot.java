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

import java.time.LocalTime;
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

//    public GetUpdatedSlotResponse execute(UUID slotId, UpdateSlotRequest request) {
//        String username = this.currentUserContext.getUsername();
//        try {
//            User user = this.userRepository.findByEmail(username);
//            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());
//
//            Slot slot = this.slotRepository.getById(slotId);
//            LocalTime newStart = LocalTime.parse(request.startHour());
//            LocalTime newEnd = LocalTime.parse(request.endHour());
//            HoursRange newRange = HoursRange.of(newStart, newEnd);
//
//            if (request.medicalConcernIds().isEmpty()) {
//                throw new AtLeastOneMedicalConcernException();
//            }
//            List<MedicalConcern> newConcerns = this.medicalConcernRepository.findAllById(request.medicalConcernIds());
//            slot.update(newRange, newConcerns);
//
//            List<Slot> existingSlots = this.slotRepository.findAllByDoctorIdAndDate(doctor.getId(), slot.getDate());
//            slot.validateAgainstOverlaps(existingSlots.stream()
//                    .filter(validSlot -> !validSlot.getId().equals(slot.getId()))
//                    .toList());
//
//            this.slotRepository.save(slot, doctor.getId());
//
//            return new GetUpdatedSlotResponse();
//        } catch (DomainException e) {
//            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
//        }
//    }
}
