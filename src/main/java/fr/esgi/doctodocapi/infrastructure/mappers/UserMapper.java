package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getPhoneNumber(),
                userEntity.isEmailVerified(),
                userEntity.isFirstConnexion(),
                userEntity.isDoubleAuthActive(),
                userEntity.getDoubleAuthCode(),
                userEntity.getCreatedAt()
        );

    }
}
