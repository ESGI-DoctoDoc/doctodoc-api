package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetAllMedicalRecordDocuments;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class GetAllMedicalRecordDocuments implements IGetAllMedicalRecordDocuments {
    private final MedicalRecordRepository medicalRecordRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DocumentResponseMapper documentResponseMapper;

    public GetAllMedicalRecordDocuments(MedicalRecordRepository medicalRecordRepository, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, PatientRepository patientRepository, DocumentResponseMapper documentResponseMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.documentResponseMapper = documentResponseMapper;
    }

    public final List<GetDocumentResponse> process(String type, int page, int size) {
        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);

            Optional<Patient> patient = this.patientRepository.getByUserId(user.getId());
            if (patient.isEmpty()) throw new PatientNotFoundException();

            List<Document> documents;

            if (type != null) {
                DocumentType documentType = DocumentType.fromValue(type);
                documents = this.medicalRecordRepository.getDocumentsByTypeAndPatientId(documentType.name(), patient.get().getId(), page, size);
            } else {
                documents = this.medicalRecordRepository.getDocumentsByPatientId(patient.get().getId(), page, size);
            }
            return documents.stream().map(this.documentResponseMapper::toDto).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
