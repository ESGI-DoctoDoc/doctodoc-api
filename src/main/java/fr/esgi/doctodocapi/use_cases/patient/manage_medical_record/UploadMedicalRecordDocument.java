package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.use_cases.MedicalRecordFolders;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.CreateMedicalRecordDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUploadMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UploadMedicalRecordDocument implements IUploadMedicalRecordDocument {
    private final FileStorageService fileStorageService;
    private final GetPatientFromContext getPatientFromContext;
    private final MedicalRecordRepository medicalRecordRepository;

    public UploadMedicalRecordDocument(FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext, MedicalRecordRepository medicalRecordRepository) {
        this.fileStorageService = fileStorageService;
        this.getPatientFromContext = getPatientFromContext;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public CreateMedicalRecordDocumentResponse createDocument(SaveDocumentRequest saveDocumentRequest) {
        try {

            Patient patient = this.getPatientFromContext.get();

            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patient.getId());

            DocumentType type = DocumentType.fromValue(saveDocumentRequest.type());
            String prefixPath = MedicalRecordFolders.FOLDER_ROOT + patient.getId() + MedicalRecordFolders.FOLDER_MEDICAL_FILE;

            Document document = Document.init(saveDocumentRequest.filename(), prefixPath, type, patient.getUserId());
            document.setPath(prefixPath + document.getId());

            medicalRecord.addDocument(document);

            this.medicalRecordRepository.save(medicalRecord);

            return new CreateMedicalRecordDocumentResponse(document.getId());

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }

    public GetUrlUploadResponse getPresignedUrlToUpload(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patient.getId());
            Document document = medicalRecord.getById(id);
            String url = this.fileStorageService.getPresignedUrlToUpload(document.getPath());
            return new GetUrlUploadResponse(url);
        } catch (DocumentNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
