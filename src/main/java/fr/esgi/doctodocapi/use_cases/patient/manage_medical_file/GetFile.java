package fr.esgi.doctodocapi.use_cases.patient.manage_medical_file;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetFileResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file.IGetFile;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;

import java.util.UUID;

public class GetFile implements IGetFile {
    private final FileStorageService uploadFile;

    public GetFile(FileStorageService uploadFile) {
        this.uploadFile = uploadFile;
    }

    public GetFileResponse execute(UUID id) {
        // todo fetch id of patient
        // todo with id of doc, fetch name
        String fileName = "Sujet.pdf";
        String url = this.uploadFile.getFile("patient_id/" + fileName);
        return new GetFileResponse(id, fileName, url);
    }
}
