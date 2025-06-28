package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetDocumentMedicalRecordContent;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetDocumentMedicalRecordContent implements IGetDocumentMedicalRecordContent {
    private final MedicalRecordRepository medicalRecordRepository;
    private final FileStorageService fileStorageService;
    private final IGetPatientFromContext getPatientFromContext;

    public GetDocumentMedicalRecordContent(MedicalRecordRepository medicalRecordRepository, FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.fileStorageService = fileStorageService;
        this.getPatientFromContext = getPatientFromContext;
    }

    public GetDocumentResponse process(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patient.getId());

            Document document = medicalRecord.getById(id);
            String url = this.fileStorageService.getFile(document.getPath());

            return new GetDocumentResponse(id, document.getName(), url);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
