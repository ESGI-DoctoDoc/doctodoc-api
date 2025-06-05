package fr.esgi.doctodocapi.model.samples;

import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserSample {
    public static User get() {
        return new User(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                Email.of("john.doe@example.com"),
                Password.of("Méday1234#"),
                PhoneNumber.of("+33601020304"),
                true,
                false,
                null,
                LocalDateTime.of(2023, 1, 1, 12, 0)
        );
    }
}
