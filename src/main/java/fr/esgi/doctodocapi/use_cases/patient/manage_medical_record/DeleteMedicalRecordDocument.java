package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IDeleteMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DeleteMedicalRecordDocument implements IDeleteMedicalRecordDocument {
    private final MedicalRecordRepository medicalRecordRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final IGetPatientFromContext getPatientFromContext;

    public DeleteMedicalRecordDocument(MedicalRecordRepository medicalRecordRepository, DocumentRepository documentRepository, FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.getPatientFromContext = getPatientFromContext;
    }

    public void process(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();

            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patient.getId());
            Document document = medicalRecord.getById(id);
            document.delete(patient.getUserId());

            this.documentRepository.delete(document);
            this.fileStorageService.delete(document.getPath());

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
