package fr.esgi.doctodocapi.infrastructure.security.service;

import fr.esgi.doctodocapi.domain.use_cases.exceptions.authentication.AuthenticationException;
import fr.esgi.doctodocapi.domain.use_cases.exceptions.authentication.AuthentificationMessageException;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.AdminJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;
    private final AdminJpaRepository adminJpaRepository;

    public CustomUserDetailsService(UserJpaRepository userJpaRepository, AdminJpaRepository adminJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.adminJpaRepository = adminJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String authMethod) throws UsernameNotFoundException {

        Optional<UserEntity> userFoundByEmail = this.userJpaRepository.findByEmailIgnoreCase(authMethod);
        Optional<UserEntity> userFoundByPhoneNumber = this.userJpaRepository.findByPhoneNumber(authMethod);

        if (userFoundByEmail.isEmpty() && userFoundByPhoneNumber.isEmpty()) {
            throw new AuthenticationException(AuthentificationMessageException.BAD_CREDENTIALS);
        }

        UserEntity userEntity = userFoundByPhoneNumber.orElseGet(userFoundByEmail::get);

        GrantedAuthority grantedAuthority = this.isAdminOrUser(userEntity);

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                List.of(grantedAuthority)
        );
    }

    public GrantedAuthority isAdminOrUser(UserEntity userEntity) {
        String role = "ROLE_USER";

        UUID userId = userEntity.getId();
        boolean isAdminFoundById = this.adminJpaRepository.existsById(userId);

        if (isAdminFoundById) {
            role = "ROLE_ADMIN";
        }

        return new SimpleGrantedAuthority(role);
    }

}
