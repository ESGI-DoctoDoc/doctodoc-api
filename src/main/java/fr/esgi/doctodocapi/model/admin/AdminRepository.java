package fr.esgi.doctodocapi.model.admin;

import java.util.List;
import java.util.UUID;

public interface AdminRepository {
    boolean existsByUserId(UUID userId);

    List<UUID> getAll();
}
