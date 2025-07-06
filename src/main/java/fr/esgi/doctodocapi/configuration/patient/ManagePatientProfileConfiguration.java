package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor_recruitment.DoctorRecruitmentRepository;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.manage_account.GetInformations;
import fr.esgi.doctodocapi.use_cases.patient.manage_account.ManageProfile;
import fr.esgi.doctodocapi.use_cases.patient.manage_account.OnBoardingPatient;
import fr.esgi.doctodocapi.use_cases.patient.manage_account.ProfilePresentationMapper;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IGetInformations;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IManageProfile;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_account.IOnBoardingPatient;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.NotificationService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.TokenFcmRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagePatientProfileConfiguration {
    @Bean
    public IGetInformations getInformations(GetPatientFromContext patientFromContext, TokenFcmRepository tokenFcmRepository, ProfilePresentationMapper profilePresentationMapper, NotificationService notificationService) {
        return new GetInformations(patientFromContext, tokenFcmRepository, profilePresentationMapper, notificationService);
    }

    @Bean
    public IManageProfile manageProfile(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, ProfilePresentationMapper profilePresentationMapper) {
        return new ManageProfile(getCurrentUserContext, userRepository, patientRepository, profilePresentationMapper);
    }

    @Bean
    public IOnBoardingPatient onBoardingPatient(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, DoctorRecruitmentRepository doctorRecruitmentRepository, MedicalRecordRepository medicalRecordRepository, ProfilePresentationMapper profilePresentationMapper) {
        return new OnBoardingPatient(getCurrentUserContext, userRepository, patientRepository, doctorRepository, doctorRecruitmentRepository, medicalRecordRepository, profilePresentationMapper);
    }
}
