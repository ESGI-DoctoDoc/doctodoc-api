package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentUser;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetMedicalRecordDocumentTracesResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetMedicalRecordDocumentTraces;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GetMedicalRecordDocumentTraces implements IGetMedicalRecordDocumentTraces {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final GetPatientFromContext getPatientFromContext;

    public GetMedicalRecordDocumentTraces(PatientRepository patientRepository, DoctorRepository doctorRepository, MedicalRecordRepository medicalRecordRepository, GetPatientFromContext getPatientFromContext) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.getPatientFromContext = getPatientFromContext;
    }

    public List<GetMedicalRecordDocumentTracesResponse> getAll(UUID documentId) {
        try {
            Patient patient = this.getPatientFromContext.get();
            MedicalRecord medicalRecord = this.medicalRecordRepository.getByPatientId(patient.getId());
            Document document = medicalRecord.getById(documentId);

            List<DocumentTrace> traces = document.getTraces();

            return traces.stream().map(trace -> {
                GetDocumentUser user = this.getGetUploadedByUser(trace.userId());

                return new GetMedicalRecordDocumentTracesResponse(
                        trace.type().getValue(),
                        trace.description(),
                        user,
                        trace.date()
                );
            }).toList();


        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    private GetDocumentUser getGetUploadedByUser(UUID id) {
        GetDocumentUser user = null;

        Optional<Patient> patientFound = this.patientRepository.getByUserId(id);
        if (patientFound.isPresent()) {
            Patient patient = patientFound.get();
            user = new GetDocumentUser(patient.getFirstName(), patient.getLastName());
        } else {
            Optional<Doctor> doctorFound = this.doctorRepository.getByUserId(id);
            if (doctorFound.isPresent()) {
                Doctor doctor = doctorFound.get();
                user = new GetDocumentUser(doctor.getPersonalInformations().getFirstName(), doctor.getPersonalInformations().getLastName());
            }
        }
        return user;
    }
}
