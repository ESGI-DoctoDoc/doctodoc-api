package fr.esgi.doctodocapi.configuration.patient;

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
    public IGetAllDocuments getAllDocuments(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository, DocumentResponseMapper documentResponseMapper) {
        return new GetAllDocuments(medicalRecordRepository, getCurrentUserContext, userRepository, patientRepository, documentResponseMapper);
    }

    @Bean
    public IUploadDocument uploadDocument(FileStorageService fileStorageService, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        return new UploadDocument(fileStorageService, getCurrentUserContext, userRepository, patientRepository, medicalRecordRepository);
    }

    @Bean
    public IGetDocumentMedicalRecordContent getDocumentMedicalRecordContent(FileStorageService fileStorageService, DocumentRepository documentRepository) {
        return new GetDocumentMedicalRecordContentMedicalRecordContent(documentRepository, fileStorageService);
    }

    @Bean
    public IGetDocumentDetail getDocumentDetail(DocumentRepository documentRepository) {
        return new GetDocumentDetail(documentRepository);
    }

    @Bean
    public IDeleteDocument deleteDocument(FileStorageService fileStorageService, DocumentRepository documentRepository) {
        return new DeleteDocument(documentRepository, fileStorageService);
    }
}
