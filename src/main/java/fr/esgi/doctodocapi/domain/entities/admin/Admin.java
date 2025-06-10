package fr.esgi.doctodocapi.domain.entities.admin;

import fr.esgi.doctodocapi.domain.entities.doctor.Doctor;
import fr.esgi.doctodocapi.domain.entities.user.User;
import fr.esgi.doctodocapi.domain.entities.vo.email.Email;
import fr.esgi.doctodocapi.domain.entities.vo.password.Password;
import fr.esgi.doctodocapi.domain.entities.vo.phone_number.PhoneNumber;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an Admin user in the system.
 * <p>
 * Extends the generic User class by adding admin-specific behavior.
 * Mainly responsible for validating doctor accounts.
 */
public class Admin extends User {
    private UUID id;

    /**
     * Constructs an Admin instance with the given details.
     *
     * @param id                 the unique identifier of the admin
     * @param email              the admin's email value object
     * @param password           the admin's password value object
     * @param phoneNumber        the admin's phone number value object
     * @param isEmailVerified    indicates if the admin's email is verified
     * @param isDoubleAuthActive indicates if two-factor authentication is active for the admin
     * @param doubleAuthCode     the current two-factor authentication code
     * @param createdAt          timestamp of when the admin account was created
     */
    public Admin(UUID id, Email email, Password password, PhoneNumber phoneNumber, boolean isEmailVerified, boolean isDoubleAuthActive, String doubleAuthCode, LocalDateTime createdAt) {
        super(id, email, password, phoneNumber, isEmailVerified, isDoubleAuthActive, doubleAuthCode, createdAt);
    }

    /**
     * Validates the given doctor account by setting its verified status to true.
     *
     * @param doctor the doctor whose account will be validated
     */
    public static void validateDoctorAccount(Doctor doctor) {
        doctor.setVerified(true);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the user ID from the superclass User.
     *
     * @return the UUID of the user (Admin)
     */
    public UUID getUserId() {
        return super.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                '}';
    }
}
