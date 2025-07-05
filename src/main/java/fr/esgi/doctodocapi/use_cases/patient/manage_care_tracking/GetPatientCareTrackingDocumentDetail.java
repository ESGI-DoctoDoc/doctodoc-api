package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentDetailOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentUser;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackingDocumentDetail;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

public class GetPatientCareTrackingDocumentDetail implements IGetPatientCareTrackingDocumentDetail {
    private final CareTrackingRepository careTrackingRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final IGetPatientFromContext getPatientFromContext;

    public GetPatientCareTrackingDocumentDetail(CareTrackingRepository careTrackingRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, GetPatientFromContext getPatientFromContext) {
        this.careTrackingRepository = careTrackingRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.getPatientFromContext = getPatientFromContext;
    }

    public GetDocumentDetailOfCareTrackingResponse process(UUID careTrackingId, UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();

            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(careTrackingId, patient);
            CareTrackingDocument document = careTracking.getById(id);

            GetDocumentUser user = getGetUploadedByUser(document.getDocument().getUploadedBy());

            return new GetDocumentDetailOfCareTrackingResponse(
                    id,
                    document.getDocument().getType().getValue(),
                    document.getDocument().getName(),
                    document.getDocument().getPath(),
                    document.isShared(),
                    document.getDocument().getUploadedAt(),
                    user
            );

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
