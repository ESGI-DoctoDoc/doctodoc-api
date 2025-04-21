package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.UserMapper;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findByEmailOrPhoneNumber(String email, String phoneNumber) {
        UserEntity userFoundByMail = this.userJpaRepository.findByEmailIgnoreCaseOrPhoneNumber(email, phoneNumber).orElseThrow(UserNotFoundException::new);
        return this.userMapper.toDomain(userFoundByMail);
    }

    @Override
    public void updateDoubleAuthCode(String code, UUID userId) {
        UserEntity userEntity = this.userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userEntity.setDoubleAuthCode(code);
        userEntity.setDoubleAuthActive(true);
        this.userJpaRepository.save(userEntity);
    }
}
