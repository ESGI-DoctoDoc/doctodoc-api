package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AdminJpaRepository;
import fr.esgi.doctodocapi.model.admin.AdminRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Implementation of the AdminRepository interface.
 * This service provides functionality to manage administrator data,
 * allowing the application to verify administrator privileges.
 */
@Repository
public class AdminRepositoryImpl implements AdminRepository {
    /**
     * Repository for accessing administrator data in the database.
     */
    private final AdminJpaRepository adminJpaRepository;

    /**
     * Constructs an AdminRepositoryImpl with the required repository.
     *
     * @param adminJpaRepository Repository for administrator data access
     */
    public AdminRepositoryImpl(AdminJpaRepository adminJpaRepository) {
        this.adminJpaRepository = adminJpaRepository;
    }

    /**
     * Checks if an administrator exists with the specified user ID.
     *
     * @param userId The unique identifier of the user to check
     * @return true if an administrator with the specified user ID exists, false otherwise
     */
    @Override
    public boolean existsByUserId(UUID userId) {
        return this.adminJpaRepository.existsByUser_id(userId);
    }
}
