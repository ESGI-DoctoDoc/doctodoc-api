package fr.esgi.doctodocapi.infrastructure.impl;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.UserMapper;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        UserEntity userFoundByMail = this.userJpaRepository.findByEmailIgnoreCase(email).orElseThrow(UserNotFoundException::new);
        return this.userMapper.toDomain(userFoundByMail);
    }

    @Override
    public void save(User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword().getValue());
        UserEntity entity = this.userMapper.toEntity(user, hashPassword);
        this.userJpaRepository.save(entity);
    }

    @Override
    public boolean isExistUser(String email, String phoneNumber) {
        return this.userJpaRepository.findByEmailIgnoreCaseOrPhoneNumber(email, phoneNumber).isPresent();
    }

    @Override
    public void validateEmail(UUID userId) {
        UserEntity userFoundByMail = this.userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userFoundByMail.setEmailVerified(true);
        this.userJpaRepository.save(userFoundByMail);
    }
}
