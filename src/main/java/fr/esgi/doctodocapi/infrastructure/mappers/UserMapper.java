package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.domain.entities.user.User;
import fr.esgi.doctodocapi.domain.entities.vo.email.Email;
import fr.esgi.doctodocapi.domain.entities.vo.phone_number.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                Email.of(userEntity.getEmail()),
                null, // to decode
                PhoneNumber.of(userEntity.getPhoneNumber()),
                userEntity.isEmailVerified(),
                userEntity.isDoubleAuthActive(),
                userEntity.getDoubleAuthCode(),
                userEntity.getCreatedAt()
        );
    }

    public UserEntity toEntity(User user, String hashPassword) {
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail().getValue());
        entity.setPassword(hashPassword);
        entity.setPhoneNumber(user.getPhoneNumber().getValue());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setDoubleAuthActive(user.isDoubleAuthActive());
        entity.setDoubleAuthCode(user.getDoubleAuthCode());
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }
}
