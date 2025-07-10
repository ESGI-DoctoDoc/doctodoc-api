package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot;

import fr.esgi.doctodocapi.infrastructure.mappers.SlotResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotCannotBeInThePastException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot.ExceptionalSlotRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveExceptionalSlot;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

public class SaveExceptionalSlot implements ISaveExceptionalSlot {
    private final SlotRepository slotRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final SlotResponseMapper slotResponseMapper;

    public SaveExceptionalSlot(SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, SlotResponseMapper slotResponseMapper) {
        this.slotRepository = slotRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.slotResponseMapper = slotResponseMapper;
    }

    public GetSlotResponse execute(ExceptionalSlotRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<MedicalConcern> concerns = this.medicalConcernRepository.findAllById(request.medicalConcerns());

            Slot newSlot = Slot.create(request.start(), request.startHour(), request.endHour(), concerns);

            LocalDate slotDate = request.start();
            if (slotDate.isBefore(LocalDate.now())) {
                throw new SlotCannotBeInThePastException();
            }

            List<Slot> existingSlots = this.slotRepository.findAllByDoctorIdAndDate(doctor.getId(), slotDate);
            newSlot.validateAgainstOverlaps(existingSlots);

            doctor.getCalendar().addSlot(newSlot);

            Slot saved = this.slotRepository.save(newSlot, doctor.getId());
            return this.slotResponseMapper.present(saved);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}