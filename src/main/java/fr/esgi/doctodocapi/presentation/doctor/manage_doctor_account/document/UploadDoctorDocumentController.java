package fr.esgi.doctodocapi.presentation.doctor.manage_doctor_account.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.document.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetUploadedDoctorDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetUrlUploadForDoctorDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IUploadDoctorOnboardingDocument;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("doctors/onboarding")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class UploadDoctorDocumentController {
    private final IUploadDoctorOnboardingDocument uploadDoctorOnboardingDocument;

    public UploadDoctorDocumentController(IUploadDoctorOnboardingDocument uploadDoctorOnboardingDocument) {
        this.uploadDoctorOnboardingDocument = uploadDoctorOnboardingDocument;
    }

    @PostMapping("documents")
    @ResponseStatus(HttpStatus.CREATED)
    public GetUploadedDoctorDocumentResponse createDocument(@RequestBody SaveDocumentRequest request) {
        return this.uploadDoctorOnboardingDocument.execute(request);
    }

    @GetMapping("documents/upload-url/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetUrlUploadForDoctorDocumentResponse getPresignedUrl(@PathVariable UUID id) {
        return this.uploadDoctorOnboardingDocument.getPresignedUrlToUpload(id);
    }
}
