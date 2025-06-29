package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking.*;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.DocumentResponseMapper;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.*;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagePatientCareTrackingConfiguration {
    @Bean
    public IGetPatientCareTrackings patientCareTrackings(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, AppointmentRepository appointmentRepository) {
        return new GetPatientCareTrackings(careTrackingRepository, getPatientFromContext, appointmentRepository);
    }

    @Bean
    public IGetPatientCareTrackingDetailed patientCareTrackingDetailed(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, GetPatientCareTrackingDetailedMapper getPatientCareTrackingDetailedMapper, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        return new GetPatientCareTrackingDetailed(careTrackingRepository, getPatientFromContext, getPatientCareTrackingDetailedMapper, doctorRepository, appointmentRepository);
    }

    @Bean
    public IUploadPatientCareTrackingDocument uploadPatientCareTrackingDocument(GetPatientFromContext getPatientFromContext, CareTrackingRepository careTrackingRepository, FileStorageService fileStorageService) {
        return new UploadPatientCareTrackingDocument(getPatientFromContext, careTrackingRepository, fileStorageService);
    }

    @Bean
    public IGetPatientDocumentCareTrackingContent getPatientDocumentCareTrackingContent(CareTrackingRepository careTrackingRepository, FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext) {
        return new GetPatientDocumentCareTrackingContent(careTrackingRepository, fileStorageService, getPatientFromContext);
    }

    @Bean
    public IGetAllCareTrackingDocuments getAllCareTrackingDocuments(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, DocumentResponseMapper documentResponseMapper) {
        return new GetAllCareTrackingDocuments(careTrackingRepository, getPatientFromContext, documentResponseMapper);
    }

    @Bean
    public IGetPatientCareTrackingDocumentDetail getPatientCareTrackingDocumentDetail(CareTrackingRepository careTrackingRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, GetPatientFromContext getPatientFromContext) {
        return new GetPatientCareTrackingDocumentDetail(careTrackingRepository, patientRepository, doctorRepository, getPatientFromContext);
    }

    @Bean
    public IDeletePatientCareTrackingDocument deletePatientCareTrackingDocument(CareTrackingRepository careTrackingRepository, DocumentRepository documentRepository, FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext) {
        return new DeletePatientCareTrackingDocument(careTrackingRepository, documentRepository, fileStorageService, getPatientFromContext);
    }

    @Bean
    public IUpdatePatientCareTrackingDocument updatePatientCareTrackingDocument(IGetPatientFromContext getPatientFromContext, CareTrackingRepository careTrackingRepository) {
        return new UpdatePatientCareTrackingDocument(getPatientFromContext, careTrackingRepository);
    }
}
