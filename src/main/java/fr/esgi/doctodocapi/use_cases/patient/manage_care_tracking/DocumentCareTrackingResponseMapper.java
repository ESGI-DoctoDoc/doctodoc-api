package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentsOfCareTrackingResponse;
import org.springframework.stereotype.Service;

@Service
public class DocumentCareTrackingResponseMapper {
    public GetDocumentsOfCareTrackingResponse toDto(CareTrackingDocument document) {
        return new GetDocumentsOfCareTrackingResponse(
                document.getDocument().getId(),
                document.getDocument().getName(),
                document.getDocument().getType().getValue(),
                document.getDocument().getPath(),
                document.isShared()
        );
    }
}
