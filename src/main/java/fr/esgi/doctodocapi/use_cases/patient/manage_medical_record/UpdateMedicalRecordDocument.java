package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUpdateMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UpdateMedicalRecordDocument implements IUpdateMedicalRecordDocument {
    private final IGetPatientFromContext getPatientFromContext;
    private final MedicalRecordRepository medicalRecordRepository;


    public UpdateMedicalRecordDocument(GetPatientFromContext getPatientFromContext, MedicalRecordRepository medicalRecordRepository) {
        this.getPatientFromContext = getPatientFromContext;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public GetDocumentResponse process(UUID id, SaveDocumentRequest saveDocumentRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patient.getId());

            Document document = medicalRecord.getById(id);

            Document newDocument = Document.copyOf(document);
            newDocument.setId(document.getId());
            newDocument.update(saveDocumentRequest.filename(), DocumentType.fromValue(saveDocumentRequest.type()), patient.getUserId());

            medicalRecord.updateDocument(document, newDocument);

            this.medicalRecordRepository.save(medicalRecord);
            return new GetDocumentResponse(newDocument.getId(), newDocument.getName(), document.getType().getValue(), newDocument.getPath());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
