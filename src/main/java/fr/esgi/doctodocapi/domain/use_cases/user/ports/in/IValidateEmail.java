package fr.esgi.doctodocapi.domain.use_cases.user.ports.in;

import java.util.UUID;

public interface IValidateEmail {
    void validate(UUID userId);
}
