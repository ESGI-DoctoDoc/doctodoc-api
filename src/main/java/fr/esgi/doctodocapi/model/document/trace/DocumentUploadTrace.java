package fr.esgi.doctodocapi.model.document.trace;

import java.util.UUID;

public class DocumentUploadTrace extends DocumentTrace {
    private static final DocumentTraceType TYPE = DocumentTraceType.CREATION;
    private static final String DESCRIPTION = "Le document a été créé.";

    public DocumentUploadTrace(UUID userId) {
        super(TYPE, DESCRIPTION, userId);
    }

}
