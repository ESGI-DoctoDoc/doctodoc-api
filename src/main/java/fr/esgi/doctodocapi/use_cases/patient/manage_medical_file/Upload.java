package fr.esgi.doctodocapi.use_cases.patient.manage_medical_file;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file.IUpload;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;

public class Upload implements IUpload {
    private final FileStorageService uploadFile;

    public Upload(FileStorageService uploadFile) {
        this.uploadFile = uploadFile;
    }

    public GetUrlUploadResponse execute(String filename) {
        String url = this.uploadFile.getPresignedUrlToUpload("patient_id/" + filename);
        return new GetUrlUploadResponse(url);
    }
}
