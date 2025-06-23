package fr.esgi.doctodocapi.configuration.patient;

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
    public IUploadMedicalRecordDocument uploadMedicalRecordDocument(FileStorageService fileStorageService, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository, DocumentRepository documentRepository) {
        return new UploadMedicalRecordDocument(fileStorageService, getCurrentUserContext, userRepository, patientRepository, medicalRecordRepository, documentRepository);
    }

    @Bean
    public IGetDocumentMedicalRecordContent getDocumentMedicalRecordContent(FileStorageService fileStorageService, DocumentRepository documentRepository) {
        return new GetDocumentMedicalRecordContent(documentRepository, fileStorageService);
    }

    @Bean
    public IGetMedicalRecordDocumentDetail getMedicalRecordDocumentDetail(DocumentRepository documentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        return new GetMedicalRecordDocumentDetail(documentRepository, patientRepository, doctorRepository);
    }

    @Bean
    public IUpdateMedicalRecordDocument updateMedicalRecordDocument(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository, DocumentRepository documentRepository) {
        return new UpdateMedicalRecordDocument(userRepository, patientRepository, getCurrentUserContext, medicalRecordRepository, documentRepository);
    }

    @Bean
    public IDeleteMedicalRecordDocument deleteMedicalRecordDocument(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, FileStorageService fileStorageService, DocumentRepository documentRepository) {
        return new DeleteMedicalRecordDocument(documentRepository, fileStorageService, getCurrentUserContext, userRepository);
    }
}
