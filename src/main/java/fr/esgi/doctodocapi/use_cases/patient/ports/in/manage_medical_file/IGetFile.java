package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_file;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetFileResponse;

import java.util.UUID;

public interface IGetFile {
    GetFileResponse execute(UUID id);
}
