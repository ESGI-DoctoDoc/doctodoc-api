package fr.esgi.doctodocapi.domain.entities.admin;

import java.util.UUID;

public interface AdminRepository {
    boolean existsByUserId(UUID userId);
}
