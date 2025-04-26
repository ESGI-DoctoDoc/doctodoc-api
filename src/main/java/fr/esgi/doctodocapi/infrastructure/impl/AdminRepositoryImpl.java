package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AdminJpaRepository;
import fr.esgi.doctodocapi.model.admin.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminRepositoryImpl implements AdminRepository {
    private final AdminJpaRepository adminJpaRepository;

    public AdminRepositoryImpl(AdminJpaRepository adminJpaRepository) {
        this.adminJpaRepository = adminJpaRepository;
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return this.adminJpaRepository.existsByUser_id(userId);
    }
}
