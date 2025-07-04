package fr.esgi.doctodocapi.model.care_tracking.documents;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.use_cases.CareTrackingFolders;

import java.util.Objects;
import java.util.UUID;

public class CareTrackingDocument {
    private final Document document;
    private boolean isShared;

    public CareTrackingDocument(Document document, boolean isShared) {
        this.document = document;
        this.isShared = isShared;
    }

    public static CareTrackingDocument create(String name, String typeValue, UUID patientId, UUID careTrackingId) {
        return create(name, typeValue, patientId, patientId, careTrackingId);
    }

    public static CareTrackingDocument create(String name, String typeValue, UUID patientId, UUID uploadedBy, UUID careTrackingId) {
        DocumentType type = DocumentType.fromValue(typeValue);
        String prefixPath = CareTrackingFolders.FOLDER_ROOT + patientId + CareTrackingFolders.FOLDER_CARE_TRACKING_FILE;

        Document document = Document.init(name, prefixPath, type, uploadedBy);
        document.setPath(prefixPath + careTrackingId + "/" + document.getId());

        return new CareTrackingDocument(document, false);
    }

    public static CareTrackingDocument copyOf(CareTrackingDocument document) {
        return new CareTrackingDocument(Document.copyOf(document.getDocument()), document.isShared);
    }

    public Document getDocument() {
        return document;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CareTrackingDocument that = (CareTrackingDocument) o;
        return Objects.equals(document.getId(), that.document.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(document.getId());
    }
}
