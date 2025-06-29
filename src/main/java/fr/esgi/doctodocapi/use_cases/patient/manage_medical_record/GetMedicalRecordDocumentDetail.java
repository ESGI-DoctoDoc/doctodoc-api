package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUploadedByUser;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetMedicalRecordDocumentDetail;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

public class GetMedicalRecordDocumentDetail implements IGetMedicalRecordDocumentDetail {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final IGetPatientFromContext getPatientFromContext;

    public GetMedicalRecordDocumentDetail(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, GetPatientFromContext getPatientFromContext) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.getPatientFromContext = getPatientFromContext;
    }

    public GetDocumentDetailResponse process(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();

            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patient.getId());
            Document document = medicalRecord.getById(id);

            GetUploadedByUser user = getGetUploadedByUser(document.getUploadedBy());

            return new GetDocumentDetailResponse(
                    id,
                    document.getType().getValue(),
                    document.getName(),
                    document.getPath(),
                    document.getUploadedAt(),
                    user
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
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
