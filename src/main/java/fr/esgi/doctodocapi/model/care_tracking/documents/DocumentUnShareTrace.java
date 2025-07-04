package fr.esgi.doctodocapi.model.care_tracking.documents;

import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import fr.esgi.doctodocapi.model.document.trace.DocumentTraceType;

import java.util.UUID;

public class DocumentUnShareTrace extends DocumentTrace {
    private static final DocumentTraceType TYPE = DocumentTraceType.REMOVE_PERMISSION;
    private static final String DESCRIPTION = "Le document n'est plus partagé avec tous les docteurs du suivi de dossier.";

    public DocumentUnShareTrace(UUID userId) {
        super(TYPE, DESCRIPTION, userId);
    }

}
