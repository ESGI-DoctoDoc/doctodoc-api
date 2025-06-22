package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordNotFoundException;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUpdateMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

public class UpdateMedicalRecordDocument implements IUpdateMedicalRecordDocument {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DocumentRepository documentRepository;


    public UpdateMedicalRecordDocument(UserRepository userRepository, PatientRepository patientRepository, GetCurrentUserContext getCurrentUserContext, MedicalRecordRepository medicalRecordRepository, DocumentRepository documentRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.medicalRecordRepository = medicalRecordRepository;
        this.documentRepository = documentRepository;
    }

    public GetDocumentResponse process(UUID id, SaveDocumentRequest saveDocumentRequest) {
        try {
            Document document = this.documentRepository.getById(id);

            Document newDocument = Document.copyOf(document);
            newDocument.setId(document.getId());
            newDocument.update(saveDocumentRequest.filename(), DocumentType.fromValue(saveDocumentRequest.type()));

            MedicalRecord medicalRecord = getMedicalRecord();
            medicalRecord.updateDocument(document, newDocument);

            this.medicalRecordRepository.save(medicalRecord);
            return new GetDocumentResponse(newDocument.getId(), newDocument.getName(), newDocument.getPath());
        } catch (DocumentNotFoundException | MedicalRecordNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }

    private UUID getPatientId() {
        String username = this.getCurrentUserContext.getUsername();

        User user = this.userRepository.findByEmail(username);
        Optional<Patient> patient = this.patientRepository.getByUserId(user.getId());
        if (patient.isEmpty()) throw new PatientNotFoundException();
        return patient.get().getId();
    }

    private MedicalRecord getMedicalRecord() {
        UUID patientId = getPatientId();

        return this.medicalRecordRepository.getByPatientId(patientId);
    }


}
