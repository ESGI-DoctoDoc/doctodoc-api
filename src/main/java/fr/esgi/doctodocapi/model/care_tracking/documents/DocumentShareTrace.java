package fr.esgi.doctodocapi.model.care_tracking.documents;

import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import fr.esgi.doctodocapi.model.document.trace.DocumentTraceType;

import java.util.UUID;

public class DocumentShareTrace extends DocumentTrace {
    private static final DocumentTraceType TYPE = DocumentTraceType.ADD_PERMISSION;
    private static final String DESCRIPTION = "Le document a été partagé avec tous les docteurs du suivi de dossier.";

    public DocumentShareTrace(UUID userId) {
        super(TYPE, DESCRIPTION, userId);
    }

}
