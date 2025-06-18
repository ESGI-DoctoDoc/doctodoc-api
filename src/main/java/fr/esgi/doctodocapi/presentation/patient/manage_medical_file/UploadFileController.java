package fr.esgi.doctodocapi.presentation.patient.manage_medical_file;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file.IUpload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class UploadFileController {
    private final IUpload iUpload;

    public UploadFileController(IUpload iUpload) {
        this.iUpload = iUpload;
    }

    @PostMapping("/upload")
    public void upload() {
        this.iUpload.execute();
    }
}
