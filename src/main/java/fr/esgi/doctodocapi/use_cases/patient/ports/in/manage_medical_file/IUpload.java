package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetUrlUploadResponse;

public interface IUpload {
    GetUrlUploadResponse execute(String filename);
}
