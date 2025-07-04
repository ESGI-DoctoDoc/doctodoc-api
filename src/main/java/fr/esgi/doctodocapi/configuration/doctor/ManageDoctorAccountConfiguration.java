package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.GetDoctorProfileResponseMapper;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.payment.subscription.DoctorSubscriptionRepository;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.AddressAutocomplete;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.GetDoctorInformation;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.GetDoctorProfileInformation;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.OnboardingDoctorProcess;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.document.GetDoctorOnboardingDocumentContent;
import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.document.UploadDoctorOnboardingDocument;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_information.IGetDoctorInformation;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IAutocompleteAddress;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IGetDoctorProfileInformation;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.IOnboardingDoctor;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IGetDoctorOnboardingDocumentContent;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IUploadDoctorOnboardingDocument;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.AddressAutoCompleteInput;
import fr.esgi.doctodocapi.use_cases.doctor.ports.out.AddressCoordinatesFetcher;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageDoctorAccountConfiguration {

    @Bean
    public IOnboardingDoctor onboardingDoctorProcess(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DocumentRepository documentRepository, SpecialityRepository specialityRepository, AddressCoordinatesFetcher addressCoordinatesFetcher) {
        return new OnboardingDoctorProcess(doctorRepository, userRepository, getCurrentUserContext, documentRepository, specialityRepository, addressCoordinatesFetcher);
    }

    @Bean
    public IGetDoctorInformation getDoctorInformation(DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository) {
        return new GetDoctorInformation(userRepository, doctorRepository, getCurrentUserContext, doctorSubscriptionRepository);
    }

    @Bean
    public IUploadDoctorOnboardingDocument uploadDoctorOnboardingDocument(UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DocumentRepository documentRepository, FileStorageService fileStorageService) {
        return new UploadDoctorOnboardingDocument(userRepository, getCurrentUserContext, documentRepository, fileStorageService);
    }

    @Bean
    public IGetDoctorOnboardingDocumentContent getDoctorOnboardingDocumentContent(FileStorageService fileStorageService, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DocumentRepository documentRepository) {
        return new GetDoctorOnboardingDocumentContent(fileStorageService, documentRepository, userRepository, getCurrentUserContext);
    }

    @Bean
    public IAutocompleteAddress autocompleteAddress(AddressAutoCompleteInput input) {
        return new AddressAutocomplete(input);
    }

    @Bean
    public IGetDoctorProfileInformation getDoctorProfileInformation(UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext, DoctorSubscriptionRepository doctorSubscriptionRepository, GetDoctorProfileResponseMapper doctorProfileResponseMapper) {
        return new GetDoctorProfileInformation(userRepository, doctorRepository, getCurrentUserContext, doctorSubscriptionRepository, doctorProfileResponseMapper);
    }
}
