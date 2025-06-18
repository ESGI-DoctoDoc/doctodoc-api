package fr.esgi.doctodocapi.presentation.patient.manage_medical_file;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file.IUpload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients/medical-file")
public class UploadFileController {
    private final IUpload iUpload;

    public UploadFileController(IUpload iUpload) {
        this.iUpload = iUpload;
    }

    @GetMapping("/upload-url/{filename}")
    public GetUrlUploadResponse upload(@PathVariable String filename) {
        return this.iUpload.execute(filename);
    }
}
