package fr.esgi.doctodocapi.presentation.doctor.manage_doctor_account.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetDocumentForDoctorOnboardingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IGetDoctorOnboardingDocumentContent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class GetDoctorDocumentController {

    private final IGetDoctorOnboardingDocumentContent getDoctorOnboardingDocumentContent;

    public GetDoctorDocumentController(IGetDoctorOnboardingDocumentContent getDoctorOnboardingDocumentContent) {
        this.getDoctorOnboardingDocumentContent = getDoctorOnboardingDocumentContent;
    }

    @GetMapping("onboarding/documents/{documentId}")
    public GetDocumentForDoctorOnboardingResponse getDocumentContent(@PathVariable UUID documentId) {
        return this.getDoctorOnboardingDocumentContent.execute(documentId);
    }
}