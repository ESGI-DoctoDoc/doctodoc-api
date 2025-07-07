package fr.esgi.doctodocapi.presentation.doctor.manage_doctor_account.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.DeleteDoctorDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IDeleteDoctorDocument;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/doctors/onboarding")
public class DeleteDoctorDocumentController {
    private final IDeleteDoctorDocument deleteDoctorDocument;

    public DeleteDoctorDocumentController(IDeleteDoctorDocument deleteDoctorDocument) {
        this.deleteDoctorDocument = deleteDoctorDocument;
    }

    @DeleteMapping("/documents/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public DeleteDoctorDocumentResponse delete(@PathVariable UUID id) {
        return this.deleteDoctorDocument.execute(id);
    }
}
