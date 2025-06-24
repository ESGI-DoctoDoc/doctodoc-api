package fr.esgi.doctodocapi.model.document.permission;

import java.util.Objects;
import java.util.UUID;

public record Permission(PermissionType type, UUID doctorId) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(doctorId, that.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(doctorId);
    }
}
