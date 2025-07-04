package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingResponseMapper;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.doctor_managing_care_tracking.GetCareTrackings;
import fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.doctor_managing_care_tracking.InitializeCareTracking;
import fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document.GetCareTrackingDocumentContent;
import fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document.UploadCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_managing_care_tracking.IGetCareTrackings;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.doctor_managing_care_tracking.IInitializeCareTracking;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IGetCareTrackingDocumentContent;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IUploadCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DoctorManagingCareTrackingConfiguration {

    @Bean
    public IInitializeCareTracking initializeCareTracking(CareTrackingRepository careTrackingRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        return new InitializeCareTracking(careTrackingRepository, doctorRepository, userRepository, getCurrentUserContext, patientRepository, appointmentRepository);
    }

    @Bean
    public IGetCareTrackings getCareTrackings(CareTrackingRepository careTrackingRepository, DoctorRepository doctorRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, CareTrackingResponseMapper careTrackingResponseMapper) {
        return new GetCareTrackings(careTrackingRepository, doctorRepository, userRepository, getCurrentUserContext, careTrackingResponseMapper);
    }

    @Bean
    public IUploadCareTrackingDocument uploadCareTrackingDocument(CareTrackingRepository careTrackingRepository, FileStorageService uploadFile, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorRepository doctorRepository, DocumentRepository documentRepository) {
        return new UploadCareTrackingDocument(careTrackingRepository, uploadFile, userRepository, getCurrentUserContext, doctorRepository, documentRepository);
    }

    @Bean
    public IGetCareTrackingDocumentContent getCareTrackingDocumentContent(CareTrackingRepository careTrackingRepository, FileStorageService fileStorageService, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext) {
        return new GetCareTrackingDocumentContent(careTrackingRepository, fileStorageService, userRepository, doctorRepository, getCurrentUserContext);
    }
}
