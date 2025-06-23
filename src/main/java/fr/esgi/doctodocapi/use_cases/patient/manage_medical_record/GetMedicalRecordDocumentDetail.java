package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUploadedByUser;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetMedicalRecordDocumentDetail;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

public class GetMedicalRecordDocumentDetail implements IGetMedicalRecordDocumentDetail {
    private final DocumentRepository documentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public GetMedicalRecordDocumentDetail(DocumentRepository documentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.documentRepository = documentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public GetDocumentDetailResponse process(UUID id) {
        try {
            Document document = this.documentRepository.getById(id);
            GetUploadedByUser user = getGetUploadedByUser(document.getUploadedBy());

            return new GetDocumentDetailResponse(
                    id,
                    document.getType().getValue(),
                    document.getName(),
                    document.getPath(),
                    document.getUploadedAt(),
                    user
            );

        } catch (DocumentNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }

    private GetUploadedByUser getGetUploadedByUser(UUID id) {
        GetUploadedByUser user = null;

        Optional<Patient> patientFound = this.patientRepository.getByUserId(id);
        if (patientFound.isPresent()) {
            Patient patient = patientFound.get();
            user = new GetUploadedByUser(patient.getFirstName(), patient.getLastName());
        } else {
            Optional<Doctor> doctorFound = this.doctorRepository.getByUserId(id);
            if (doctorFound.isPresent()) {
                Doctor doctor = doctorFound.get();
                user = new GetUploadedByUser(doctor.getPersonalInformations().getFirstName(), doctor.getPersonalInformations().getLastName());
            }
        }
        return user;
    }
}
