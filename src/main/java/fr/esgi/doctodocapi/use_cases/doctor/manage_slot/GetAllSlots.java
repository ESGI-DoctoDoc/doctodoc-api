package fr.esgi.doctodocapi.use_cases.doctor.manage_slot;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response.GetSlotResponse;
import fr.esgi.doctodocapi.infrastructure.mappers.SlotResponseMapper;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Use case for retrieving all the slots of a doctor within a date range (1 week),
 * with pagination support.
 */
@Service
public class GetAllSlots {

    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final SlotResponseMapper slotResponseMapper;

    public GetAllSlots(SlotRepository slotRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, SlotResponseMapper slotResponseMapper) {
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.slotResponseMapper = slotResponseMapper;
    }

    /**
     * Retrieves all slots for the current doctor starting from a given date for 7 days,
     * with support for pagination.
     *
     * @param page       The page number to retrieve
     * @param size       The number of results per page
     * @param startDate  The start date (inclusive) for the slot search
     * @return A list of slot response DTOs
     * @throws ApiException if a domain-level validation fails
     */
    public List<GetSlotResponse> getAll(int page, int size, LocalDate startDate) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User doctor = this.userRepository.findByEmail(username);

            LocalDate endDate = startDate.plusDays(6);

            List<Slot> slots = this.slotRepository.findAllByDoctorIdAndDateBetween(doctor.getId(), startDate, endDate, page, size);
            return this.slotResponseMapper.presentAll(slots);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}