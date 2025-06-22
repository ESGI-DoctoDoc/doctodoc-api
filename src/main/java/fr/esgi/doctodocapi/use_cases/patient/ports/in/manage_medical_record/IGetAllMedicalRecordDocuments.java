package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentResponse;

import java.util.List;

public interface IGetAllMedicalRecordDocuments {
    List<GetDocumentResponse> process(int page, int size);
}
