package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.SlotResponseMapper;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.RecurrentSlotRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot.SaveExceptionalSlot;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot.SaveWeeklySlots;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot.SaveMonthlySlots;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_slot.GetSlots;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveExceptionalSlot;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveWeeklySlots;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.ISaveMonthlySlots;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_slot.IGetSlots;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageSlotConfiguration {

    @Bean
    public ISaveWeeklySlots saveWeeklySlots(SlotRepository slotRepository, GetCurrentUserContext getCurrentUserContext, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, RecurrentSlotRepository recurrentSlotRepository, SlotResponseMapper slotResponseMapper, DoctorRepository doctorRepository) {
        return new SaveWeeklySlots(slotRepository, medicalConcernRepository, userRepository, getCurrentUserContext, recurrentSlotRepository, slotResponseMapper, doctorRepository);
    }

    @Bean
    public ISaveMonthlySlots saveMonthlySlots(SlotRepository slotRepository, GetCurrentUserContext getCurrentUserContext, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, RecurrentSlotRepository recurrentSlotRepository, SlotResponseMapper slotResponseMapper, DoctorRepository doctorRepository) {
        return new SaveMonthlySlots(slotRepository, medicalConcernRepository, userRepository, getCurrentUserContext, recurrentSlotRepository, slotResponseMapper, doctorRepository);
    }

    @Bean
    public IGetSlots getAllSlots(SlotRepository slotRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, SlotResponseMapper slotResponseMapper, DoctorRepository doctorRepository) {
        return new GetSlots(slotRepository, userRepository, getCurrentUserContext, slotResponseMapper, doctorRepository);
    }

    @Bean
    public ISaveExceptionalSlot saveExceptionalSlot(SlotRepository slotRepository, MedicalConcernRepository medicalConcernRepository, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, SlotResponseMapper slotResponseMapper) {
        return new SaveExceptionalSlot(slotRepository, medicalConcernRepository, userRepository, doctorRepository, getCurrentUserContext, slotResponseMapper);
    }
}