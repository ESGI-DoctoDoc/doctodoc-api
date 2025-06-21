package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.DocumentResponseMapper;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.GetAllDocuments;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.GetDocument;
import fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.UploadDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetAllDocuments;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUploadDocument;
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
    public IGetDocument getDocument(FileStorageService fileStorageService, DocumentRepository documentRepository) {
        return new GetDocument(documentRepository, fileStorageService);
    }


}
