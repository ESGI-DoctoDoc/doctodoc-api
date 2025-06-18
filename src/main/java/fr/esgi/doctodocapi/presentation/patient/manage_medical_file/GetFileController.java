package fr.esgi.doctodocapi.presentation.patient.manage_medical_file;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetFileResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file.IGetFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class GetFileController {
    private final IGetFile iGetFile;

    public GetFileController(IGetFile iGetFile) {
        this.iGetFile = iGetFile;
    }

    @GetMapping("medical-file/file/get/{id}")
    public GetFileResponse get(@PathVariable UUID id) {
        return this.iGetFile.execute(id);
    }
}
