package fr.esgi.doctodocapi.model.document;

import fr.esgi.doctodocapi.model.document.permission.Permission;
import fr.esgi.doctodocapi.model.document.permission.PermissionType;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Document {
    private UUID id;
    private String name;
    private String path;
    private DocumentType type;
    private List<DocumentTrace> traces;
    private List<Permission> permissions;

    public Document(UUID id, String name, String path, DocumentType type, List<DocumentTrace> traces, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.traces = traces;
        this.permissions = permissions;
    }

    public static Document init(String name, String url, DocumentType type) {
        // todo add trace
        return new Document(
                UUID.randomUUID(),
                name,
                url,
                type,
                List.of(),
                List.of()
        );
    }

    public void delete() {
        // todo add trace
    }

    // manage permission
    public void addPermission(PermissionType type, UUID doctorId) {
        Permission permission = new Permission(type, doctorId);
        if (this.permissions.contains(permission)) throw new PermissionAlreadyExistException();
        this.permissions.add(permission);
        // todo add trace
    }

    public void deletePermission(Permission permission) {
        this.permissions.remove(permission);
        // todo add trace
    }

    public void modifyPermission(Permission oldPermission, Permission newPermission) {
        this.permissions.remove(oldPermission);
        this.permissions.add(newPermission);
        // todo add trace
    }

    public List<Permission> getPermissions() {
        return List.copyOf(permissions);
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<DocumentTrace> getTraces() {
        return List.copyOf(traces);
    }

    public void setTraces(List<DocumentTrace> traces) {
        this.traces = traces;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
