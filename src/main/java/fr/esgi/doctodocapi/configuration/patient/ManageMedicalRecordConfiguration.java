package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.*;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.*;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManageMedicalRecordConfiguration {

    @Bean
    public IGetAllMedicalRecordDocuments getAllMedicalRecordDocuments(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository, DocumentResponseMapper documentResponseMapper) {
        return new GetAllMedicalRecordDocuments(medicalRecordRepository, getCurrentUserContext, userRepository, patientRepository, documentResponseMapper);
    }

    @Bean
    public IUploadMedicalRecordDocument uploadMedicalRecordDocument(FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext, MedicalRecordRepository medicalRecordRepository, DocumentRepository documentRepository) {
        return new UploadMedicalRecordDocument(fileStorageService, getPatientFromContext, medicalRecordRepository, documentRepository);
    }

    @Bean
    public IGetDocumentMedicalRecordContent getDocumentMedicalRecordContent(FileStorageService fileStorageService, MedicalRecordRepository medicalRecordRepository, GetPatientFromContext getPatientFromContext) {
        return new GetDocumentMedicalRecordContent(medicalRecordRepository, fileStorageService, getPatientFromContext);
    }

    @Bean
    public IGetMedicalRecordDocumentDetail getMedicalRecordDocumentDetail(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, GetPatientFromContext getPatientFromContext) {
        return new GetMedicalRecordDocumentDetail(medicalRecordRepository, patientRepository, doctorRepository, getPatientFromContext);
    }

    @Bean
    public IUpdateMedicalRecordDocument updateMedicalRecordDocument(GetPatientFromContext getPatientFromContext, MedicalRecordRepository medicalRecordRepository) {
        return new UpdateMedicalRecordDocument(getPatientFromContext, medicalRecordRepository);
    }

    @Bean
    public IDeleteMedicalRecordDocument deleteMedicalRecordDocument(MedicalRecordRepository medicalRecordRepository, DocumentRepository documentRepository, FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext) {
        return new DeleteMedicalRecordDocument(medicalRecordRepository, documentRepository, fileStorageService, getPatientFromContext);
    }

    @Bean
    public IGetMedicalRecordDocumentTraces getMedicalRecordDocumentTraces(PatientRepository patientRepository, DoctorRepository doctorRepository, MedicalRecordRepository medicalRecordRepository, GetPatientFromContext getPatientFromContext) {
        return new GetMedicalRecordDocumentTraces(patientRepository, doctorRepository, medicalRecordRepository, getPatientFromContext);
    }
}
