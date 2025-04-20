package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
}
