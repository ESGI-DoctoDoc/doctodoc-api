package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import java.util.UUID;

public interface IManageDocumentVisibility {
    void share(UUID careTrackingId, UUID documentId);
}
