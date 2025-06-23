package fr.esgi.doctodocapi.model.document.trace;

import java.util.UUID;

public class DocumentUpdateTrace extends DocumentTrace {
    private static final DocumentTraceType TYPE = DocumentTraceType.UPDATE;
    private static final String DESCRIPTION = "Le document a été modifié.";

    public DocumentUpdateTrace(UUID userId) {
        super(TYPE, DESCRIPTION, userId);
    }

}
