package fr.esgi.doctodocapi.model.document.trace;

import java.util.UUID;

public class DocumentDeletionTrace extends DocumentTrace {
    private static final DocumentTraceType TYPE = DocumentTraceType.DELETION;
    private static final String DESCRIPTION = "Le document a été supprimé.";

    public DocumentDeletionTrace(UUID userId) {
        super(TYPE, DESCRIPTION, userId);
    }

}
