package fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot;

import fr.esgi.doctodocapi.infrastructure.mappers.SlotResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentStatus;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.IGetAllSlots;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Use case for retrieving all the slots of a doctor within a date range (1 week),
 * with pagination support.
 */
public class GetAllSlots implements IGetAllSlots {
    private static final List<String> VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN = Arrays.asList(
            AppointmentStatus.CONFIRMED.getValue(),
            AppointmentStatus.UPCOMING.getValue(),
            AppointmentStatus.WAITING_ROOM.getValue()
    );

    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final SlotResponseMapper slotResponseMapper;
    private final DoctorRepository doctorRepository;


    public GetAllSlots(SlotRepository slotRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, SlotResponseMapper slotResponseMapper, DoctorRepository doctorRepository) {
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.slotResponseMapper = slotResponseMapper;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Retrieves all slots for the current doctor starting from a given date for 7 days,
     * with support for pagination.
     *
     * @param page      The page number to retrieve
     * @param size      The number of results per page
     * @param startDate The start date (inclusive) for the slot search
     * @return A list of slot response DTOs
     * @throws ApiException if a domain-level validation fails
     */
    public List<GetSlotResponse> getAll(int page, int size, LocalDate startDate) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            List<Slot> slots;
            if (startDate != null) {
                LocalDate endDate = startDate.plusDays(6);
                slots = this.slotRepository.findVisibleByDoctorIdAndDateBetween(
                        doctor.getId(),
                        startDate,
                        endDate,
                        VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN,
                        page,
                        size
                );
            } else {
                slots = this.slotRepository.findVisibleByDoctorIdAndDateAfter(
                        doctor.getId(),
                        LocalDate.now(),
                        VALID_STATUSES_FOR_DELETED_MEDICAL_CONCERN,
                        page,
                        size
                );
            }

            return this.slotResponseMapper.presentAll(slots);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}