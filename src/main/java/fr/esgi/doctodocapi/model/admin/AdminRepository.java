package fr.esgi.doctodocapi.model.admin;

import java.util.UUID;

public interface AdminRepository {
    boolean existsByUserId(UUID userId);
}
