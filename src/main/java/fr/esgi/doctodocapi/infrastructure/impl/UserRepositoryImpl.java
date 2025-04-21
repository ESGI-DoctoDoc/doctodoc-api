package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.UserMapper;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findByEmail(String email) {
        UserEntity userFoundByMail = this.userJpaRepository.findByEmailIgnoreCase(email).orElseThrow(UserNotFoundException::new);
        return this.userMapper.toDomain(userFoundByMail);
    }

    @Override
    public void updateDoubleAuthCode(String code, User user) {
        UserEntity userEntity = this.userJpaRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        userEntity.setDoubleAuthCode(code);
        userEntity.setDoubleAuthActive(true);
        this.userJpaRepository.save(userEntity);
    }
}
