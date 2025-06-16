package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.AbsenceResponseMapper;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceRepository;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidationService;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence.DeleteAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IDeleteAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence.GetAbsences;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.IGetAbsences;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence.SaveRangeAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.ISaveRangeAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.manage_calendar.manage_absence.SaveSingleDayAbsence;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_absence.ISaveSingleDayAbsence;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageAbsenceConfiguration {

    @Bean
    public IDeleteAbsence deleteAbsence(AbsenceRepository absenceRepository) {
        return new DeleteAbsence(absenceRepository);
    }

    @Bean
    public IGetAbsences getAbsences(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper) {
        return new GetAbsences(getCurrentUserContext, absenceRepository, userRepository, absenceResponseMapper);
    }

    @Bean
    public ISaveRangeAbsence saveRangeAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository, AbsenceValidationService absenceValidationService) {
        return new SaveRangeAbsence(absenceRepository, getCurrentUserContext, userRepository, absenceResponseMapper, doctorRepository, absenceValidationService);
    }

    @Bean
    public ISaveSingleDayAbsence saveSingleDayAbsence(AbsenceRepository absenceRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, AbsenceResponseMapper absenceResponseMapper, DoctorRepository doctorRepository, AbsenceValidationService absenceValidationService) {
        return new SaveSingleDayAbsence(absenceRepository, getCurrentUserContext, userRepository, absenceResponseMapper, doctorRepository, absenceValidationService);
    }
}