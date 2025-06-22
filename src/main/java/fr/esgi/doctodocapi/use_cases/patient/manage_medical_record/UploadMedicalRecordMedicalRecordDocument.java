package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.document.Document;
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
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUploadMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.text.Normalizer;
import java.util.Optional;
import java.util.UUID;

import static fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.MedicalRecordFolders.FOLDER_MEDICAL_FILE;
import static fr.esgi.doctodocapi.use_cases.patient.manage_medical_record.MedicalRecordFolders.FOLDER_ROOT;

public class UploadMedicalRecordMedicalRecordDocument implements IUploadMedicalRecordDocument {
    private final FileStorageService uploadFile;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public UploadMedicalRecordMedicalRecordDocument(FileStorageService uploadFile, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        this.uploadFile = uploadFile;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public GetUrlUploadResponse getPresignedUrlToUpload(String filename) {
        String username = this.getCurrentUserContext.getUsername();
        String filenameCleaned = removeAccent(filename);

        try {
            User user = this.userRepository.findByEmail(username);

            Optional<Patient> patient = this.patientRepository.getByUserId(user.getId());
            if (patient.isEmpty()) throw new PatientNotFoundException();

            UUID patientId = patient.get().getId();

            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patientId);
            medicalRecord.verifyIfFilenameAlreadyExist(filenameCleaned);

            String path = FOLDER_ROOT + patientId + FOLDER_MEDICAL_FILE + filenameCleaned;

            String url = this.uploadFile.getPresignedUrlToUpload(path);
            return new GetUrlUploadResponse(url);

        } catch (UserNotFoundException | PatientNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }

    public void createDocument(SaveDocumentRequest saveDocumentRequest) {
        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);

            Optional<Patient> patient = this.patientRepository.getByUserId(user.getId());
            if (patient.isEmpty()) throw new PatientNotFoundException();

            UUID patientId = patient.get().getId();

            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patientId);

            String filename = removeAccent(saveDocumentRequest.filename());
            DocumentType type = DocumentType.fromValue(saveDocumentRequest.type());
            String path = FOLDER_ROOT + patientId + FOLDER_MEDICAL_FILE + filename;

            Document document = Document.init(filename, path, type);
            medicalRecord.addDocument(document);

            this.medicalRecordRepository.save(medicalRecord);

        } catch (UserNotFoundException | PatientNotFoundException | MedicalRecordNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }

    private String removeAccent(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
