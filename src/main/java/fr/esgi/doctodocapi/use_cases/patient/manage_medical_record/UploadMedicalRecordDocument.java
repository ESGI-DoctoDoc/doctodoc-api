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
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.CreateMedicalRecordDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUploadMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.MedicalRecordFolders.FOLDER_MEDICAL_FILE;
import static fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.MedicalRecordFolders.FOLDER_ROOT;

public class UploadMedicalRecordDocument implements IUploadMedicalRecordDocument {
    private final FileStorageService uploadFile;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DocumentRepository documentRepository;

    public UploadMedicalRecordDocument(FileStorageService uploadFile, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository, DocumentRepository documentRepository) {
        this.uploadFile = uploadFile;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.documentRepository = documentRepository;
    }

    public CreateMedicalRecordDocumentResponse createDocument(SaveDocumentRequest saveDocumentRequest) {
        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);

            Optional<Patient> patient = this.patientRepository.getByUserId(user.getId());
            if (patient.isEmpty()) throw new PatientNotFoundException();

            UUID patientId = patient.get().getId();

            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patientId);

            DocumentType type = DocumentType.fromValue(saveDocumentRequest.type());
            String prefixPath = FOLDER_ROOT + patientId + FOLDER_MEDICAL_FILE;

            Document document = Document.init(saveDocumentRequest.filename(), prefixPath, type, user.getId());
            document.setPath(prefixPath + document.getId());

            medicalRecord.addDocument(document);

            this.medicalRecordRepository.save(medicalRecord);

            return new CreateMedicalRecordDocumentResponse(document.getId());

        } catch (UserNotFoundException | PatientNotFoundException | MedicalRecordNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }

    public GetUrlUploadResponse getPresignedUrlToUpload(UUID id) {
        try {
            Document document = this.documentRepository.getById(id);
            String url = this.uploadFile.getPresignedUrlToUpload(document.getPath());
            return new GetUrlUploadResponse(url);
        } catch (DocumentNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
