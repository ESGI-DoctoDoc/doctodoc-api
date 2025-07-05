package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking;

import java.util.UUID;

public interface IDeletePatientCareTrackingDocument {
    void process(UUID careTrackingId, UUID id);
}
